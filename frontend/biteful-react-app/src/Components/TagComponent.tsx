import { Tag, X } from "lucide-react";


type TagProps = {
    label: string;
    editable?: boolean;
    onRemove?: () => void;
}

function TagComponent({ label, editable = false, onRemove }: TagProps) {
    return (
        <div className="inline-flex items-center gap-1 rounded-full border px-2 py-0.5 text-xs">
            <Tag className="w-3 h-3" />
            <div>{label}</div>

            {editable && (
                <button onClick={onRemove}>
                    <X className="w-3 h-3" />
                </button>
            )}
        </div>
    );
}

export default TagComponent;