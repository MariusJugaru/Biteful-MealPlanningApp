import { type LucideIcon, Eye, EyeOff } from "lucide-react"
import { useState } from "react";

type Props = {
    image: LucideIcon;
    val: string;
    field: string;
    placeHolder: string;
    onChange: (value: string) => void;
    showToggle?: boolean;
    className?: string;
    error?: string;
}

function TextInputShort({
    image: Icon,
    val,
    field,
    placeHolder,
    onChange,
    showToggle = false,
    className = "",
    error = "",
}: Props) {
    const [showText, setShowText] = showToggle ? useState(false) : useState(true);

    const inputType = showText ? "text" : "password";

    return (
        <div className={className}>
            <div>
                <div className="font-medium">{field}</div>
                {error && (
                    <div className="text-red-600 text-sm">{error}</div>
                )}
            </div>
            
            <div className="relative">
                <Icon className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-[#868686]" />
                <input
                    type={inputType}
                    value={val}
                    placeholder={placeHolder}
                    onChange={(e) => {onChange(e.target.value)}}
                    className="w-full bg-[#eeeeee] border rounded-md px-3 py-2 pl-10 outline-none focus:ring-2 focus:ring-[#b1b1b1] transition-all duration-200"
                />
                {showToggle && (
                    <button
                        type="button"
                        onClick={() => setShowText((s) => !s)}
                        className="absolute right-3 top-1/2 -translate-y-1/2 text-[#868686]"
                    >
                        {showText ? <Eye /> : <EyeOff />}
                    </button>
                )}
            </div>
        </div>
        
    );
}

export default TextInputShort;