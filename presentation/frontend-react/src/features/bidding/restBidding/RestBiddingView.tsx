import { useState } from "react";
import type { FormEvent } from "react";

import type { System } from "../../../core/api/bffApiClient";
import type { RestBiddingForm } from "./restBidding.types";
import { useRestBidding } from "./useRestBidding";

const initialForm: RestBiddingForm = {
  northHand: "",
  southHand: "",
  bidding: "",
  system: "POLISH_CLUB"
};

export function RestBidding() {
  const [form, setForm] = useState<RestBiddingForm>(initialForm);
  const [touched, setTouched] = useState(false);

  const { submit, loading, error, result, reset } = useRestBidding();

  const northValid = form.northHand.trim().length > 0;
  const southValid = form.southHand.trim().length > 0;
  const formValid = northValid && southValid;

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setTouched(true);

    if (!formValid) return;

    await submit(form);
  }

  function handleReset() {
    setForm(initialForm);
    setTouched(false);
    reset();
  }

  return (
    <section className="section-card">
      <form className="form" onSubmit={handleSubmit}>
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
                    system: e.target.value as System
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
              <div className="result-panel error-panel">{error}</div>
            )}

            {result && (
              <details className="result-panel" open>
                <summary>{result.bidding}</summary>
                <p>{result.explanation}</p>
              </details>
            )}
          </div>

          <footer className="card-actions">
            <button
              className="primary-action"
              type="submit"
              disabled={loading || !formValid}
            >
              {loading ? "Calculating..." : "Calculate"}
            </button>

            <button
              className="secondary-action"
              type="button"
              onClick={handleReset}
            >
              Reset
            </button>
          </footer>
        </article>
      </form>
    </section>
  );
}
