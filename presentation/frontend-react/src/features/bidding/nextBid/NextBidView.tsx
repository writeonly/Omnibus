import { useState } from "react";
import type { FormEvent } from "react";
import { useRecommendBidMutation } from "./nextBidApi";
import { nextBidSchema } from "./nextBid.schema";

const initialForm = {
  hand: "",
  bidding: "",
  system: "POLISH_CLUB" as const
};

export function NextBid() {
  const [form, setForm] = useState(initialForm);
  const [touched, setTouched] = useState(false);

  const [recommendBid, { data, error, isLoading }] =
    useRecommendBidMutation();

  const formValid = form.hand.trim().length > 0;

  async function submit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setTouched(true);

    const validation = nextBidSchema.safeParse(form);

    if (!validation.success) return;

    await recommendBid(form);
  }

  function reset() {
    setForm(initialForm);
    setTouched(false);
  }

  return (
    <section className="section-card">
      <form className="form" onSubmit={submit}>
        <article className="bid-card">
          <header className="card-header">
            <h1>Bid recommender</h1>
            <p>Enter hand and bidding sequence</p>
          </header>

          <div className="form-layout">
            <label className="field">
              <span>Hand</span>
              <textarea
                rows={4}
                value={form.hand}
                onBlur={() => setTouched(true)}
                onChange={(e) =>
                  setForm({ ...form, hand: e.target.value })
                }
              />
              {touched && !formValid ? (
                <small className="field-error">Hand is required</small>
              ) : null}
            </label>

            <label className="field">
              <span>Bidding</span>
              <textarea
                rows={4}
                value={form.bidding}
                onChange={(e) =>
                  setForm({ ...form, bidding: e.target.value })
                }
              />
            </label>

            <label className="field">
              <span>System</span>
              <select
                value={form.system}
                onChange={(e) =>
                  setForm({
                    ...form,
                    system: e.target.value as typeof form.system
                  })
                }
              >
                <option value="POLISH_CLUB">Polish Club</option>
                <option value="STANDARD_AMERICAN">Standard American</option>
              </select>
            </label>

            {error ? (
              <div className="result-panel error-panel">
                Request failed
              </div>
            ) : null}

            {data ? (
              <details className="result-panel" open>
                <summary>{data.bid}</summary>
                <p>{data.explanation}</p>
              </details>
            ) : null}
          </div>

          <footer className="card-actions">
            <button
              className="primary-action"
              type="submit"
              disabled={isLoading || !formValid}
            >
              {isLoading ? "Calculating..." : "Calculate"}
            </button>

            <button
              className="secondary-action"
              type="button"
              onClick={reset}
            >
              Reset
            </button>
          </footer>
        </article>
      </form>
    </section>
  );
}
