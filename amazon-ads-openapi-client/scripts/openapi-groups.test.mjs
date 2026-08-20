import assert from 'node:assert/strict';
import test from 'node:test';
import { openApiGroups, selectOpenApiGroups } from './openapi-groups.mjs';

test('selectOpenApiGroups defaults to all groups and accepts a selected group', () => {
    assert.deepEqual(selectOpenApiGroups([]), openApiGroups);
    assert.deepEqual(selectOpenApiGroups(['--groups=targets']).map(group => group.id), ['targets']);
    assert.throws(() => selectOpenApiGroups(['--groups=unknown']), /Unknown API group/);
});
