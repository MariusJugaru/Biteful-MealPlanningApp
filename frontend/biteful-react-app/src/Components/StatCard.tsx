import type { LucideIcon } from "lucide-react";

type StatCardProps = {
    label: string;
    value: number;
    icon?: LucideIcon;
};

function StatCard({ label, value, icon: Icon } : StatCardProps) {
    return (
        <div className="flex items-center gap-3 bg-card border rounded-lg p-6">
            {Icon && (
                <Icon className="w-8 h-8 text-muted-foreground" />
            )}

            <div>
                <p className="text-sm text-muted-foreground mb-1">{label}</p>
                <p className="text-3xl">{value}</p>
            </div>
        </div>
    )
}

export default StatCard;