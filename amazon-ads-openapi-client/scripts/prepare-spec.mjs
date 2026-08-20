import { createHash } from 'node:crypto';
import { readFile, writeFile } from 'node:fs/promises';
import { fileURLToPath } from 'node:url';
import path from 'node:path';

const workspaceDir = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const httpMethods = new Set(['get', 'put', 'post', 'delete', 'patch', 'head', 'options', 'trace']);
const componentRefPattern = /^#\/components\/([^/]+)\/(.+)$/;
const specifications = [
    {
        label: 'Campaigns',
        tag: 'Campaigns',
        sourceFile: 'AmazonAdsAPIALLMerged_prod_3p.json',
        outputFile: 'campaigns.openapi.json',
        metadataFile: 'campaigns.source.properties',
        sourceUrl: 'https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLMerged_prod_3p.json',
        title: 'Amazon Ads Campaign Management API',
        description: 'Focused Java SDK specification containing only Campaigns operations.',
        expectedOperationIds: ['CreateCampaign', 'DeleteCampaign', 'QueryCampaign', 'UpdateCampaign'],
    },
    {
        label: 'Targets',
        tag: 'Targets',
        sourceFile: 'AmazonAdsAPIALLTargetsContract_prod_3p.json',
        outputFile: 'targets.openapi.json',
        metadataFile: 'targets.source.properties',
        sourceUrl: 'https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLTargetsContract_prod_3p.json',
        title: 'Amazon Ads Targets API',
        description: 'Focused Java SDK specification containing only Targets operations.',
        expectedOperationIds: ['CreateTarget', 'DeleteTarget', 'QueryTarget', 'UpdateTarget'],
        typedOneOfObjectSchemas: ['TargetDetails', 'CreateTargetDetails'],
    },
];

function decodeJsonPointerPart(value) {
    return value.replaceAll('~1', '/').replaceAll('~0', '~');
}

function collectComponentRefs(node, refs) {
    if (Array.isArray(node)) {
        node.forEach(item => collectComponentRefs(item, refs));
        return;
    }
    if (!node || typeof node !== 'object') {
        return;
    }
    for (const [key, value] of Object.entries(node)) {
        if (key === '$ref' && typeof value === 'string') {
            const match = componentRefPattern.exec(value);
            if (match) {
                refs.add(`${decodeJsonPointerPart(match[1])}/${decodeJsonPointerPart(match[2])}`);
            }
        }
        collectComponentRefs(value, refs);
    }
}

function selectTaggedPaths(paths, tag) {
    return Object.fromEntries(Object.entries(paths).flatMap(([apiPath, pathItem]) => {
        const operations = Object.fromEntries(Object.entries(pathItem)
            .filter(([method, operation]) => httpMethods.has(method) && operation.tags?.includes(tag)));
        if (Object.keys(operations).length === 0) {
            return [];
        }
        const metadata = Object.fromEntries(Object.entries(pathItem)
            .filter(([key]) => !httpMethods.has(key)));
        return [[apiPath, { ...metadata, ...operations }]];
    }));
}

function selectReachableComponents(sourceComponents, selectedPaths) {
    const pendingRefs = new Set();
    const selectedComponents = {};
    collectComponentRefs(selectedPaths, pendingRefs);
    while (pendingRefs.size > 0) {
        const [componentKey] = pendingRefs;
        pendingRefs.delete(componentKey);
        const separatorIndex = componentKey.indexOf('/');
        const section = componentKey.slice(0, separatorIndex);
        const name = componentKey.slice(separatorIndex + 1);
        if (selectedComponents[section]?.[name]) {
            continue;
        }
        const component = sourceComponents[section]?.[name];
        if (!component) {
            throw new Error(`Component not found: #/components/${componentKey}`);
        }
        selectedComponents[section] ??= {};
        selectedComponents[section][name] = component;
        collectComponentRefs(component, pendingRefs);
    }

    const usedSecuritySchemes = new Set();
    Object.values(selectedPaths).forEach(pathItem => Object.entries(pathItem).forEach(([method, operation]) => {
        if (httpMethods.has(method)) {
            operation.security?.forEach(requirement => Object.keys(requirement).forEach(name => usedSecuritySchemes.add(name)));
        }
    }));
    if (usedSecuritySchemes.size > 0) {
        selectedComponents.securitySchemes = Object.fromEntries([...usedSecuritySchemes].map(name => {
            const securityScheme = sourceComponents.securitySchemes?.[name];
            if (!securityScheme) {
                throw new Error(`Security scheme not found: ${name}`);
            }
            return [name, securityScheme];
        }));
    }
    return selectedComponents;
}

function normalizeSourceComponents(sourceComponents, specification) {
    if (!specification.typedOneOfObjectSchemas) {
        return sourceComponents;
    }

    const normalizedComponents = structuredClone(sourceComponents);
    specification.typedOneOfObjectSchemas.forEach(schemaName => {
        const sourceSchema = normalizedComponents.schemas?.[schemaName];
        if (!Array.isArray(sourceSchema?.oneOf) || sourceSchema.oneOf.length === 0) {
            throw new Error(`Targets: expected ${schemaName} to define oneOf alternatives.`);
        }
        const properties = {};
        sourceSchema.oneOf.forEach(alternative => {
            const entries = Object.entries(alternative.properties ?? {});
            if (entries.length !== 1) {
                throw new Error(`Targets: every ${schemaName} oneOf alternative must define exactly one property.`);
            }
            const [name, schema] = entries[0];
            if (properties[name]) {
                throw new Error(`Targets: duplicate ${schemaName} property ${name}.`);
            }
            properties[name] = schema;
        });
        normalizedComponents.schemas[schemaName] = {
            type: 'object',
            properties,
            additionalProperties: true,
            description: `Typed ${schemaName}. The source contract uses one property per alternative.`,
        };
    });
    return normalizedComponents;
}

for (const specification of specifications) {
    const sourcePath = path.join(workspaceDir, 'spec', specification.sourceFile);
    const outputPath = path.join(workspaceDir, 'spec', specification.outputFile);
    const metadataPath = path.join(workspaceDir, 'spec', specification.metadataFile);
    const sourceBuffer = await readFile(sourcePath);
    const sourceSpec = JSON.parse(sourceBuffer.toString('utf8'));
    const sourceComponents = normalizeSourceComponents(sourceSpec.components, specification);
    const selectedPaths = selectTaggedPaths(sourceSpec.paths, specification.tag);
    const operationIds = Object.values(selectedPaths).flatMap(pathItem => Object.entries(pathItem)
        .filter(([method]) => httpMethods.has(method))
        .map(([, operation]) => operation.operationId))
        .sort();
    if (JSON.stringify(operationIds) !== JSON.stringify(specification.expectedOperationIds)) {
        throw new Error(`${specification.label}: expected ${specification.expectedOperationIds.join(', ')}, found ${operationIds.join(', ')}`);
    }

    const focusedSpec = {
        openapi: sourceSpec.openapi,
        info: {
            ...sourceSpec.info,
            title: specification.title,
            description: specification.description,
        },
        servers: sourceSpec.servers,
        tags: sourceSpec.tags?.filter(tag => tag.name === specification.tag),
        paths: selectedPaths,
        components: selectReachableComponents(sourceComponents, selectedPaths),
        'x-amazon-source-spec': specification.sourceUrl,
    };
    const sourceSha256 = createHash('sha256').update(sourceBuffer).digest('hex');
    await writeFile(outputPath, `${JSON.stringify(focusedSpec, null, 2)}\n`);
    await writeFile(metadataPath, [
        `sourceUrl=${specification.sourceUrl}`,
        `sourceSha256=${sourceSha256}`,
        'generatorVersion=7.24.0',
        `operations=${operationIds.join(',')}`,
        '',
    ].join('\n'));
    console.log(`Prepared ${operationIds.length} ${specification.label} operations from SHA-256 ${sourceSha256}.`);
}
