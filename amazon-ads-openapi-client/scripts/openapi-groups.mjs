export const openApiGroups = [
    {
        id: 'campaigns',
        label: 'Campaigns',
        tag: 'Campaigns',
        sourceFile: 'AmazonAdsAPIALLMerged_prod_3p.json',
        outputFile: 'campaigns.openapi.json',
        metadataFile: 'campaigns.source.properties',
        sourceUrl: 'https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLMerged_prod_3p.json',
        title: 'Amazon Ads Campaign Management API',
        description: 'Focused Java SDK specification containing only Campaigns operations.',
        expectedOperationIds: ['CreateCampaign', 'DeleteCampaign', 'QueryCampaign', 'UpdateCampaign'],
        mavenExecution: 'generate-campaign-management-client',
    },
    {
        id: 'targets',
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
        mavenExecution: 'generate-targets-client',
    },
];

export function selectOpenApiGroups(argumentsList) {
    if (argumentsList.length === 0) {
        return openApiGroups;
    }

    const [groupsOption] = argumentsList;
    if (argumentsList.length !== 1 || !groupsOption.startsWith('--groups=')) {
        throw new Error('Usage: --groups=campaigns,targets');
    }

    const selectedGroupIds = new Set(groupsOption.slice('--groups='.length).split(',').filter(Boolean));
    if (selectedGroupIds.size === 0) {
        throw new Error('At least one API group is required.');
    }

    const unknownGroupIds = [...selectedGroupIds].filter(groupId => !openApiGroups.some(group => group.id === groupId));
    if (unknownGroupIds.length > 0) {
        throw new Error(`Unknown API group: ${unknownGroupIds.join(', ')}. Available groups: ${openApiGroups.map(group => group.id).join(', ')}.`);
    }
    return openApiGroups.filter(group => selectedGroupIds.has(group.id));
}
