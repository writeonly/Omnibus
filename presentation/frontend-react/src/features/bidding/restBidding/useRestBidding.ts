import { useState } from "react";

import { bffApiClient } from "../../../core/api/bffApiClient";
import type { RestBiddingResponse } from "../../../core/api/bffApiClient";
import type { RestBiddingForm } from "./restBidding.types";

export function useRestBidding() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<RestBiddingResponse | null>(null);

  async function submit(form: RestBiddingForm) {
    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const response = await bffApiClient.recommendBidding(form);
      setResult(response);
    } catch {
      setError("Request failed");
    } finally {
      setLoading(false);
    }
  }

  function reset() {
    setError(null);
    setResult(null);
    setLoading(false);
  }

  return {
    submit,
    reset,
    loading,
    error,
    result
  };
}
