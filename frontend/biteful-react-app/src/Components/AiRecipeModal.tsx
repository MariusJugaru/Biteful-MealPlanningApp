import { useState } from "react";
import { X, Sparkles } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { config } from "../config";

type Props = {
    open: boolean;
    onClose: () => void;
}

function AiRecipeModal({ open, onClose }: Props) {
    const [prompt, setPrompt] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    if (!open) return null;

    const generateRecipe = async () => {
        if (!prompt.trim()) return;

        setLoading(true);

        try {
            const token = localStorage.getItem("token");

            const response = await fetch(
                `${config.apiUrl}/api/ai/generate`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify({
                        prompt
                    }),
                }
            );

            if (!response.ok) {
                alert("Failed to generate recipe");
                return;
            }

            const recipe = await response.json();

            navigate("/saved/add", {
                state: {
                    aiRecipe: recipe.recipe,
                },
            });
        } catch (err) {
            alert(String(err));
        } finally {
            setLoading(false);
            onClose();
        }
    }

    return (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
            <div className="bg-white w-full max-w-xl rounded-2xl shadow-xl p-6 flex flex-col gap-4">

                <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                        <Sparkles className="w-5 h-5" />
                        <h2 className="text-2xl font-semibold">
                            AI Recipe Assistant
                        </h2>
                    </div>

                    <button onClick={onClose}>
                        <X />
                    </button>
                </div>

                <p className="text-gray-500">
                    Describe the recipe you want.
                    Example:
                    "High protein chicken pasta under 700 calories"
                </p>

                <textarea
                    value={prompt}
                    onChange={(e) => setPrompt(e.target.value)}
                    placeholder="Describe your ideal recipe..."
                    className="border rounded-xl p-4 h-40 resize-none"
                />

                <button
                    onClick={generateRecipe}
                    disabled={loading}
                    className="bg-black text-white rounded-xl px-4 py-3"
                >
                    {loading ? "Generating..." : "Generate Recipe"}
                </button>
            </div>
        </div>
    );
}

export default AiRecipeModal;