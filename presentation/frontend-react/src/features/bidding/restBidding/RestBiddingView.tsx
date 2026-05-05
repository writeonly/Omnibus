import { useState } from "react";
import type { FormEvent } from "react";

import { useRecommendBiddingMutation } from "./restBiddingApi";

const initialForm = {
  northHand: "",
  southHand: "",
  bidding: "",
  system: "POLISH_CLUB" as const
};

export function RestBiddingView() {
  const [form, setForm] = useState(initialForm);
  const [touched, setTouched] = useState(false);

  const [recommendBidding, { data, error, isLoading }] =
    useRecommendBiddingMutation();

  const northValid = form.northHand.trim().length > 0;
  const southValid = form.southHand.trim().length > 0;
  const formValid = northValid && southValid;

  async function submit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setTouched(true);

    if (!formValid) return;

    await recommendBidding(form);
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
            <h1>Full bidding calculator</h1>
            <p>Enter hands and bidding sequence</p>
          </header>

          <div className="form-layout">
            <label className="field">
              <span>North</span>
              <textarea
                rows={4}
                value={form.northHand}
                onBlur={() => setTouched(true)}
                onChange={(e) =>
                  setForm({ ...form, northHand: e.target.value })
                }
              />
              {touched && !northValid && (
                <small className="field-error">
                  North hand is required
                </small>
              )}
            </label>

            <label className="field">
              <span>South</span>
              <textarea
                rows={4}
                value={form.southHand}
                onBlur={() => setTouched(true)}
                onChange={(e) =>
                  setForm({ ...form, southHand: e.target.value })
                }
              />
              {touched && !southValid && (
                <small className="field-error">
                  South hand is required
                </small>
              )}
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
                <option value="STANDARD_AMERICAN">
                  Standard American
                </option>
              </select>
            </label>

            {error && (
              <div className="result-panel error-panel">
                Request failed
              </div>
            )}

            {data && (
              <details className="result-panel" open>
                <summary>{data.bidding}</summary>
                <p>{data.explanation}</p>
              </details>
            )}
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
