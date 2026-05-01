# EKS
module "eks" {
    source  = "terraform-aws-modules/eks/aws"
    version = "~> 20.0"

    cluster_name    = "biteful-cluster"
    cluster_version = "1.35"

    vpc_id     = module.vpc.vpc_id
    subnet_ids = module.vpc.private_subnets

    cluster_endpoint_public_access = true

    enable_irsa = true

    create_iam_role = true

    # Node groups
    eks_managed_node_groups = {
        workers = {
            name = "biteful-nodes"

            instance_types = ["t3.small"]

            min_size     = 1
            max_size     = 3
            desired_size = 2

            iam_role_additional_policies = {
                AmazonEBSCSIDriverPolicy = "arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy"
            }
        }
    }

    access_entries = {
        user = {
            principal_arn = "arn:aws:iam::763253191978:user/Biteful-user"

            policy_associations = {
                full = {
                    policy_arn = "arn:aws:eks::aws:cluster-access-policy/AmazonEKSClusterAdminPolicy"
                    access_scope = {
                    type = "cluster"
                    }
                }
            }
        },
        root = {
            principal_arn = "arn:aws:iam::763253191978:root"

            policy_associations = {
                full = {
                    policy_arn = "arn:aws:eks::aws:cluster-access-policy/AmazonEKSClusterAdminPolicy"
                    access_scope = {
                    type = "cluster"
                    }
                }
            }
        }
    }

    cluster_addons = {
        coredns = {
            most_recent = true
        }

        kube-proxy = {
            most_recent = true
        }

        vpc-cni = {
            most_recent = true
        }

        aws-ebs-csi-driver = {
            most_recent = true
        }
    }

    tags = {
        Environment = "dev"
    }
}