terraform {
  required_version = ">= 1.6.0"
}

# 実 AWS へ apply するための credential は含めません。
# API Gateway / ECS / S3 / RDS / CloudWatch / IAM を設計対象として示す最小構成です。
locals {
  project_name = "ES260523874-cloud-ops-portfolio"
  region       = "ap-northeast-1"
  services = {
    api_gateway = "REST API の入口"
    ecs         = "Java API / Python worker の実行基盤"
    s3          = "実行ログと設計成果物の保管"
    rds         = "申請データの永続化候補"
    cloudwatch  = "監視とアラート"
    iam         = "最小権限ロール"
  }
}

output "portfolio_architecture_notes" {
  value = local.services
}

