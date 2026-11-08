#!/bin/bash
set -e

pms_script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if [[ $# -gt 1 || ( $# -eq 1 && "$1" != "--recreate" ) ]]; then
    printf 'Usage: %s [--recreate]\n' "$0" >&2
    exit 2
fi

# Load the existing JWT secret from the project's .env file.
set -a
source "$pms_script_dir/../.env"
set +a
: "${JWT_SECRET:?JWT_SECRET is missing from the project configuration}"

# Check the template before deleting anything.
if [[ ! -f "$pms_script_dir/cdk.out/localstack.template.json" ]]; then
    printf 'Run LocalStack.java to generate cdk.out/localstack.template.json first.\n' >&2
    exit 1
fi

# --recreate follows the course's fresh-deployment flow and resets the local databases.
if [[ "${1:-}" == "--recreate" ]]; then
    printf 'Deleting the patient-management stack and its local databases...\n'
    aws --endpoint-url=http://localhost:4566 --region us-east-1 cloudformation delete-stack \
        --stack-name patient-management
    aws --endpoint-url=http://localhost:4566 --region us-east-1 cloudformation wait stack-delete-complete \
        --stack-name patient-management
fi

# Deploy the generated configuration and supply its JwtSecret input.
aws --endpoint-url=http://localhost:4566 --region us-east-1 cloudformation deploy \
    --stack-name patient-management \
    --template-file "$pms_script_dir/cdk.out/localstack.template.json" \
    --capabilities CAPABILITY_IAM \
    --parameter-overrides "JwtSecret=$JWT_SECRET"

# Find this stack's load balancer address.
pms_lb_dns=$(aws --endpoint-url=http://localhost:4566 --region us-east-1 cloudformation describe-stacks \
    --stack-name patient-management \
    --query "Stacks[0].Outputs[?starts_with(OutputKey, 'APIGatewayServiceLoadBalancerDNS')].OutputValue | [0]" \
    --output text)

printf 'Gateway URL: http://%s:4566\n' "$pms_lb_dns"
