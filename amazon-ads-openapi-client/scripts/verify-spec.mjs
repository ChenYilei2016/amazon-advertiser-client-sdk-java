import assert from 'node:assert/strict';
import { readFile } from 'node:fs/promises';
import { fileURLToPath } from 'node:url';
import path from 'node:path';
import { selectOpenApiGroups } from './openapi-groups.mjs';

const workspaceDir = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const httpMethods = new Set(['get', 'put', 'post', 'delete', 'patch', 'head', 'options', 'trace']);
for (const specification of selectOpenApiGroups(process.argv.slice(2))) {
    const specPath = path.join(workspaceDir, 'spec', specification.outputFile);
    const spec = JSON.parse(await readFile(specPath, 'utf8'));
    const operationIds = Object.values(spec.paths).flatMap(pathItem => Object.entries(pathItem)
        .filter(([method]) => httpMethods.has(method))
        .map(([, operation]) => operation.operationId))
        .sort();
    assert.deepEqual(operationIds, specification.expectedOperationIds);

    if (specification.typedOneOfObjectSchemas) {
        specification.typedOneOfObjectSchemas.forEach(schemaName => {
            const schema = spec.components.schemas?.[schemaName];
            assert.equal(schema?.type, 'object');
            assert.equal(schema?.additionalProperties, true);
            assert.equal(schema?.oneOf, undefined);
            assert.equal(Object.keys(schema?.properties ?? {}).length, spec.components.schemas.TargetType.enum.length);
        });
    }

    const unresolvedRefs = [];
    function verifyRefs(node) {
        if (Array.isArray(node)) {
            node.forEach(verifyRefs);
            return;
        }
        if (!node || typeof node !== 'object') {
            return;
        }
        if (typeof node.$ref === 'string' && node.$ref.startsWith('#/components/')) {
            const [, , section, ...nameParts] = node.$ref.split('/');
            const name = nameParts.join('/').replaceAll('~1', '/').replaceAll('~0', '~');
            if (!spec.components[section]?.[name]) {
                unresolvedRefs.push(node.$ref);
            }
        }
        Object.values(node).forEach(verifyRefs);
    }

    verifyRefs(spec);
    assert.deepEqual(unresolvedRefs, []);
    console.log(`Verified ${operationIds.length} ${specification.label} operations and local component references.`);
}
