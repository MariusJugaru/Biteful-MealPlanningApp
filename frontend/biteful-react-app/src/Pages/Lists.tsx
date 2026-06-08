import Header from "../Components/Header";
import useFetch from "../Hooks/useFetch";
import { config } from "../config";
import { ListPlus } from "lucide-react";
import { useEffect, useState } from "react";
import AddListModal from "../Components/AddListModal";
import { useNavigate } from "react-router-dom";
import GenerateListModal from "../Components/GenerateListModal";

type List = {
    id: string;
    title: string;
    createdAt: string;
};

function Lists() {
    const token = localStorage.getItem("token");

    const navigate = useNavigate();
    
    const { data, loading, error } = useFetch<List[]>(
        `${config.apiUrl}/api/lists`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        }
    )

    const [lists, setLists] = useState<List[]>([]);

    useEffect(() => {
        if (data) {
            setLists(data);
        }
    }, [data]);

    // Create List Modal
    const [createListModal, setCreateListModal] = useState<{
        open: boolean;
    }>({
        open: false,
    });

    // Generate List Modal
    const [generateListModal, setGenerateListModal] = useState<{
        open: boolean;
    }>({
        open: false,
    });

    async function addList(title: string) {

        const response = await fetch(`${config.apiUrl}/api/lists`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: title
            }),
        });

        if (!response.ok) {
            console.error("Add meal failed");
            return;
        }

        const newList: List = await response.json();

        setLists(prev => [...prev, newList]);
    }

    async function generateList(title: string, startDate: Date, endDate: Date) {

        const start = startDate.toISOString().split("T")[0];
        const end = endDate.toISOString().split("T")[0];

        console.log(start);

        const response = await fetch(`${config.apiUrl}/api/lists/generate`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: title,
                start: start,
                end: end
            })
        });

        if (!response.ok) {
            console.error("Generate meal failed");
            return;
        }

        const newList: List = await response.json();

        setLists(prev => [...prev, newList]);
    }

    if (error) {
        return <div>{error}</div>;
    }

    return(
        <>
            <Header />
  
            <div className="mx-auto px-6 py-6">
                {/* In page header */}
                <div className="flex justify-between items-center">
                    <h3>Shopping Lists</h3>

                    <button
                        onClick={() => setGenerateListModal({ open: true })}
                        className="flex items-center gap-2 bg-white border rounded-md px-3 py-2 rounded-md text-sm font-medium"
                    >
                        Generate from Meal Plan
                    </button>
                </div>


                <div className="flex justify-between items-center mt-4">
                    <h5 className="text-[#797979]">{lists.length} lists</h5>

                    <button
                        onClick={() => setCreateListModal({ open: true })}
                        className="flex items-center gap-2 bg-black text-white px-3 py-2 rounded-md text-sm font-medium"
                    >
                        <ListPlus className="w-4 h-4" />
                        New list
                    </button>
                </div>

                
                
                {loading ? (
                    <p>Loading...</p>
                ) : (
                    // Lists cards
                    <div className="mt-4 flex flex-wrap gap-2">
                        {lists.map(list => (
                            <button
                                key={list.id}
                                onClick={() => navigate(`/lists/${list.id}`)}
                                className="w-full px-3 py-4 bg-white rounded-md border border-[#a3a3a3] text-left text-m hover:bg-gray-300"
                            >
                                {list.title}
                            </button>
                        ))}
                    </div>
                )}
            </div>

            <AddListModal 
                open={createListModal.open}
                onClose={() =>
                    setCreateListModal({open: false})
                }
                onAddList={(title) => addList(title)}
            />

            <GenerateListModal
                open={generateListModal.open}
                onClose={() => 
                    setGenerateListModal({open: false})
                }
                onAddList={(title, startDate, endDate) => generateList(title, startDate, endDate)}
            />
        </>
    );
}

export default Lists;