import { useEffect, useState } from "react";
import { DateRange } from "react-date-range";


type AddListProps = {
    open: boolean;
    onClose: () => void;
    onAddList: (title: string, startDate: Date, endDate: Date) => void;
}


function GenerateListModal({ open, onClose, onAddList } : AddListProps) {
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        const checkMobile = () => {
            setIsMobile(window.innerWidth < 768);
        };

        checkMobile();
        window.addEventListener("resize", checkMobile);

        return () => window.removeEventListener("resize", checkMobile);
    }, []);

    const [title, setTitle] = useState("");

    const [range, setRange] = useState([
        {
            startDate: new Date(),
            endDate: new Date(),
            key: "selection",
        },
    ]);

    if (!open) return null;

    return(
         <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
            
            <div className="bg-white w-fit rounded-xl p-4">
                
                <div className="flex justify-between items-center mb-4 px-2">
                    <h4>
                        Generate New Shopping List
                    </h4>

                    <button className="" onClick={onClose}>
                        ✕
                    </button>
                </div>

                <div className="flex flex-col py-2 px-2">
                    <div>
                        <h5 className="font-semibold">List Name</h5>
                        <input 
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            className="p-2 mt-2 bg-[#eeeeee] rounded-md w-full"
                            placeholder="e.g. Weekly Groceries, Party"
                        />
                    </div>
                    
                    <DateRange
                        ranges={range}
                        onChange={(item: any) => setRange([item.selection])}
                        moveRangeOnFirstSelection={false}
                        months={isMobile ? 1 : 2}
                        direction="horizontal"
                        className="py-5 w-fit"
                    />

                </div>

                <div className="mt-2 flex items-center justify-end gap-2">
                    <button
                        onClick={() => {
                            setTitle("")
                            onClose()
                        }}
                        className="flex items-center gap-2 bg-white border rounded-lg px-3 py-2 rounded-md text-sm font-medium"
                    >
                        Cancel
                    </button>

                    <button
                        onClick={() => {
                            const finalTitle = title.trim() ? title : "My New Generated List";

                            onAddList(finalTitle, range[0].startDate, range[0].endDate);

                            setTitle("");
                            onClose();
                        }}
                        className="flex items-center gap-2 bg-gray-600 text-white px-3 py-2 rounded-md text-sm font-medium"
                    >
                        Generate List
                    </button>
                </div>

            </div>

        </div>
    );
}

export default GenerateListModal;