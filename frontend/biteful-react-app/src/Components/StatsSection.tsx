import StatCard from "./StatCard";
import { Flame } from "lucide-react";

type StatsSectionProps = {
    totalWeeklyCalories: number
}

function StatsSection({ totalWeeklyCalories } : StatsSectionProps) {
    return(
        <div className="max-w-2xl mx-auto px-4 py-2">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 md:gap-4">
                <StatCard label="Weekly Calories" value={totalWeeklyCalories} icon={Flame} />
                <StatCard label="Avg. Daily Calories" value={Math.round(totalWeeklyCalories / 7)} icon={Flame} />
            </div>
        </div>
    )
}

export default StatsSection;