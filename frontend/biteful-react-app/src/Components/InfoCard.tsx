import type { LucideIcon } from "lucide-react"

type CardProps = {
    text: string;
    val: string;
    icon: LucideIcon;
    color?: string;

    editable?: boolean;
    onChange?: (value: number) => void;
}

function InfoCard({
    text,
    val,
    icon: Icon,
    color = "bg-white",
    editable = false,
    onChange,
}: CardProps) {
    return(
        <div 
            style={{
                backgroundColor: `${color}10`,
                borderColor: `${color}20`,
            }}
            className="flex items-center gap-3 p-4 rounded-lg border"
        >
            <div
                style={{ backgroundColor: `${color}10` }}
                className="w-10 h-10 flex-shrink-0 rounded-full flex items-center justify-center"
            >
                <Icon className="w-5 h-5" style={{ color }} />
            </div>
            
            <div>
                <p className="text-xs text-[#717182]">{text}</p>

                {editable ? (
                    <input
                        type="number"
                        value={val}
                        onChange={(e) => onChange?.(Number(e.target.value))}
                        className="font-semibold bg-transparent outline-none w-full border-b border-[#000000]"
                    />
                ) : (
                    <p className="font-semibold">{val}</p>
                )} 
            </div>
        </div>
    )
}

export default InfoCard;