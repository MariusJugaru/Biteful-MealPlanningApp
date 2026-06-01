import { useEffect, useState } from "react";
import useFetch from "../Hooks/useFetch";
import { config } from "../config";
import RecipeCard from "./RecipeCard";


type AddListProps = {
    open: boolean;
    onClose: () => void;
    onAddList: (title: string) => void;
}


function AddListModal({ open, onClose, onAddList } : AddListProps) {
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        const checkMobile = () => {
            setIsMobile(window.innerWidth < 768);
        };

        checkMobile();
        window.addEventListener("resize", checkMobile);

        return () => window.removeEventListener("resize", checkMobile);
    }, []);

    const token = localStorage.getItem("token");

    const [title, setTitle] = useState("");

    if (!open) return null;

    return(
         <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
            
            <div className="bg-white w-full max-w-xl rounded-xl p-4">
                
                <div className="flex justify-between items-center mb-4 px-2">
                    <h4>
                        Create New Shopping List
                    </h4>

                    <button className="" onClick={onClose}>
                        ✕
                    </button>
                </div>

                <div className="py-2 px-2">
                    <h5 className="font-semibold">List Name</h5>
                    <input 
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        className="p-2 mt-2 bg-[#eeeeee] rounded-md w-full"
                        placeholder="e.g. Weekly Groceries, Party"
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
                            if (!title.trim()) return;
                            onAddList(title);
                            setTitle("");
                            onClose();
                        }}
                        className="flex items-center gap-2 bg-gray-600 text-white px-3 py-2 rounded-md text-sm font-medium"
                    >
                        CreateList
                    </button>
                </div>

            </div>

        </div>
    );
}

export default AddListModal;