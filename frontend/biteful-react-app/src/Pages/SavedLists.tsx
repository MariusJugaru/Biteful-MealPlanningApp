import Header from "../Components/Header";
import RecipeCard from "../Components/RecipeCard";
import NavButton from "../Components/NavButton";
import useFetch from "../Hooks/useFetch";
import { config } from "../config";
import { ArrowLeft, ListPlus, Plus, Trash, Trash2 } from "lucide-react";
import AiRecipeModal from "../Components/AiRecipeModal";
import { useEffect, useState } from "react";
import AddListModal from "../Components/AddListModal";
import { useNavigate, useParams } from "react-router-dom";



type List = {
    id: string;
    title: string;
    createdAt: string;
};

type ListItem = {
    id: number;
    shoppingListId: string;
    name: string;
    quantity: number;
    unit: string;
    checked: boolean;
}

function savedList() {
    const { listId } = useParams<{ listId: string }>();

    const navigate = useNavigate();

    const token = localStorage.getItem("token");
    
    const {
        data: listData,
        loading: loadingData,
        error: errorData
    } = useFetch<List>(
        `${config.apiUrl}/api/lists/${listId}`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        }
    );

    const {
        data: itemsData,
        loading: itemsLoading,
        error: itemsError
    } = useFetch<ListItem[]>(
        `${config.apiUrl}/api/lists/${listId}/items`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        }
    )

    const [listItems, setListItems] = useState<ListItem[]>([]);

    useEffect(() => {
        if (itemsData) {
            const sorted = [...itemsData].sort((a, b) => a.id - b.id);
            setListItems(sorted);
        }
    }, [itemsData]);

    async function savedList(title: string) {

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

        // setLists(prev => [...prev, newList]);
    }

    const [name, setName] = useState("");
    const [qty, setQty] = useState<number>(0);
    const [unit, setUnit] = useState("");

    async function handleAdd() {
        if (!name.trim()) return;

        const response = await fetch(`${config.apiUrl}/api/lists/${listId}/items`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: name,
                quantity: qty,
                unit: unit
            }),
        });

        if (!response.ok) {
            console.error("Add meal failed");
            return;
        }

        const newItem: ListItem = await response.json();

        setListItems(prev => [...prev, newItem]);

        setName("");
        setQty(0);
        setUnit("");
    }

    async function onToggleItem(id: number, currentChecked: boolean) {
        const newChecked = !currentChecked;

        setListItems(prev =>
            prev.map(item =>
            item.id === id
                ? { ...item, checked: !item.checked }
                : item
            )
        );

        try {
            const response = await fetch(
                `${config.apiUrl}/api/lists/${listId}/items/${id}`,
                {
                    method: "PATCH",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        checked: newChecked
                    }),
                }
            );

            if (!response.ok) {
                throw new Error("Failed to update item");
            }
        } catch (err) {
            console.error(err);

            setListItems(prev =>
                prev.map(item =>
                    item.id === id
                        ? { ...item, checked: currentChecked }
                        : item
                )
            );
        }
    }

    async function onDeleteItem(id: number) {
        setListItems(prev =>
            prev.filter(
                item =>
                    !(item.id === id)
            )
        );

        try {
            const response = await fetch(
                `${config.apiUrl}/api/lists/${listId}/items/${id}`,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${token}`,
                    }
                }
            );

            if (!response.ok) {
                throw new Error("Failed to update item");
            }
        } catch (err) {
            console.error(err);
        }
    }

    async function onDeleteList () {
        console.log("listId =", listId);

        const response = await fetch(
                `${config.apiUrl}/api/lists/${listId}`,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${token}`,
                    }
                }
            );

        if (!response.ok) {
            console.error("Delete list failed");
            return;
        }

        navigate("/lists");
        
    }

    if (errorData) {
        return <div>{errorData}</div>;
    }
    if (itemsError) {
        return <div>{itemsError}</div>;
    }

    return(
        <>
            <Header />
  
            
            
            <div className="mx-auto px-6 py-6 select-none">
                

                {/* In page header */}
                <div className="flex justify-between items-center">
                    <h3 className="flex items-center gap-4">
                        <div className="flex items-center">
                            <NavButton to="/lists" >
                                <ArrowLeft className="w-4 h-4" />
                            </NavButton>
                        </div>
                        {listData?.title}
                        <span className="px-2 py-1 bg-gray-100 rounded-xl border text-xs">
                            {listItems.length} item{listItems.length > 1 && "s"}
                        </span>
                    </h3>

                    <button
                        onClick={() => onDeleteList()}
                        className="flex items-center gap-2 bg-red-500 text-white border px-3 py-2 rounded-md text-sm font-medium"
                    >
                        Delete List
                    </button>
                </div>
   
                
                {itemsLoading ? (
                    <p>Loading...</p>
                ) : (
                    <div className="mt-4 rounded-md border">
                        {/* ADD ITEM */}
                        <div className="px-5 mt-5 rounded-md flex gap-2 ">
                            <input
                                className="border p-2 flex-1 rounded bg-gray-100 text-gray-800"
                                placeholder="Item"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />

                            <input
                                type="number"
                                className="border p-2 w-24 rounded-md bg-gray-100 text-gray-800"
                                placeholder="0"
                                value={qty}
                                onChange={(e) => setQty(Number(e.target.value))}
                            />

                            <input
                                className="border p-2 w-24 rounded-md bg-gray-100 text-gray-800"
                                placeholder="g"
                                value={unit}
                                onChange={(e) => setUnit(e.target.value)}
                            />

                            <button
                                onClick={handleAdd}
                                className="bg-black text-white px-4 rounded-md"
                            >
                                <Plus className="w-4 h-4"/>
                            </button>
                        </div>

                        {/* LIST */}
                        <div className="p-5 shadow-lg">
                            <div className="border rounded-md">
                                {listItems.map(item => (
                                    <div
                                        key={item.id}
                                        className="flex items-center justify-between p-3 cursor-pointer border-b hover:bg-gray-50"
                                        onClick={() => onToggleItem(item.id, item.checked)}
                                    >
                                        <div className="flex items-center gap-3">
                                            <input
                                                type="checkbox"
                                                checked={item.checked}
                                                readOnly
                                            />

                                            <div className="flex items-center gap-3">
                                                <span className={item.checked ? "line-through text-gray-400" : ""}>
                                                    {item.name}
                                                </span>

                                                <span className={`text-sm ${item.checked ? "text-gray-300" : "text-gray-400"}`}>
                                                    {item.quantity !== 0 && item.quantity}
                                                    {item.unit && item.unit.trim() !== "" && ` ${item.unit}`}
                                                </span>
                                            </div>
                                        </div>

                                        <button
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                onDeleteItem(item.id);
                                            }}
                                        >
                                            <Trash2 className="w-5 h-5"/>
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                )}
            </div>

        </>
    );
}

export default savedList;