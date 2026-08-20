import { spawnSync } from 'node:child_process';
import { fileURLToPath } from 'node:url';
import path from 'node:path';
import { openApiGroups, selectOpenApiGroups } from './openapi-groups.mjs';

const workspaceDir = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const selectedGroups = selectOpenApiGroups(process.argv.slice(2));
const generateAllGroups = selectedGroups.length === openApiGroups.length;
const commands = generateAllGroups
    ? [['mvn', 'generate-sources']]
    : selectedGroups.map(group => [
        'mvn',
        '-Damazon.ads.openapi.skip-generation=false',
        '-Damazon.ads.openapi.cleanup-output=false',
        `openapi-generator:generate@${group.mavenExecution}`,
    ]);

function run(command, argumentsList) {
    const result = spawnSync(command, argumentsList, { cwd: workspaceDir, stdio: 'inherit' });
    if (result.error) {
        throw result.error;
    }
    if (result.status !== 0) {
        process.exit(result.status ?? 1);
    }
}

for (const [command, ...argumentsList] of commands) {
    run(command, argumentsList);
}
run('mvn', ['antrun:run@trim-generated-source-trailing-whitespace']);
