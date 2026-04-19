import { useEffect, useState } from "react";

function useFetch<T>(url: string | null, options?: RequestInit) {
    const [data, setData] = useState<T | null>(null);
    const [loading, setLoading] = useState(!!url);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!url) return;

        const fetchData = async () => {
            try {
                setLoading(true);

                const response = await fetch(url, options);
                const result = await response.json();

                if (!response.ok) {
                    throw new Error(result.message || "Something went wrong");
                }

                setData(result);
            } catch (err: any) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [url, JSON.stringify(options)]);

    return { data, loading, error };
}

export default useFetch;