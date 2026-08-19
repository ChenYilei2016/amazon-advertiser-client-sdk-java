import assert from 'node:assert/strict';
import { readFile } from 'node:fs/promises';
import { fileURLToPath } from 'node:url';
import path from 'node:path';

const workspaceDir = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const httpMethods = new Set(['get', 'put', 'post', 'delete', 'patch', 'head', 'options', 'trace']);
const specifications = [
    { label: 'Campaigns', file: 'campaigns.openapi.json', expectedOperationIds: ['CreateCampaign', 'DeleteCampaign', 'QueryCampaign', 'UpdateCampaign'] },
    { label: 'Targets', file: 'targets.openapi.json', expectedOperationIds: ['CreateTarget', 'DeleteTarget', 'QueryTarget', 'UpdateTarget'] },
];

for (const specification of specifications) {
    const specPath = path.join(workspaceDir, 'spec', specification.file);
    const spec = JSON.parse(await readFile(specPath, 'utf8'));
    const operationIds = Object.values(spec.paths).flatMap(pathItem => Object.entries(pathItem)
        .filter(([method]) => httpMethods.has(method))
        .map(([, operation]) => operation.operationId))
        .sort();
    assert.deepEqual(operationIds, specification.expectedOperationIds);

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
