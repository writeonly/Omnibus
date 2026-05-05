import { useState } from "react";
import { useRecommendBidMutation } from "./nextBid.api";
import { nextBidSchema } from "./nextBid.schema";

const initialForm = {
  hand: "",
  bidding: "",
  system: "POLISH_CLUB" as const
};

export function NextBidView() {
  const [form, setForm] = useState(initialForm);
  const [touched, setTouched] = useState(false);

  const [recommendBid, { data, error, isLoading }] =
    useRecommendBidMutation();

  const formValid = form.hand.trim().length > 0;

  async function submit(e: React.FormEvent) {
    e.preventDefault();
    setTouched(true);

    const parsed = nextBidSchema.safeParse(form);

    if (!parsed.success) return;

    await recommendBid(parsed.data);
  }

  return (
    <section>
      <form onSubmit={submit}>
        <textarea
          value={form.hand}
          onChange={(e) =>
            setForm({ ...form, hand: e.target.value })
          }
        />

        {error && <p>Validation/API error</p>}

        {data && (
          <div>
            <b>{data.bid}</b>
            <p>{data.explanation}</p>
          </div>
        )}

        <button disabled={isLoading || !formValid}>
          Submit
        </button>
      </form>
    </section>
  );
}
