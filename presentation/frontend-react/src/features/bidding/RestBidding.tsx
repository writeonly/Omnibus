import { useState } from 'react';
import type { FormEvent } from 'react';

import { bffApiClient } from '../../core/api/bffApiClient';
import type { RestBiddingResponse, System } from '../../core/api/bffApiClient';

interface RestBiddingForm {
  northHand: string;
  southHand: string;
  bidding: string;
  system: System;
}

const initialForm: RestBiddingForm = {
  northHand: '',
  southHand: '',
  bidding: '',
  system: 'POLISH_CLUB'
};

export function RestBidding() {
  const [form, setForm] = useState<RestBiddingForm>(initialForm);
  const [touched, setTouched] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<RestBiddingResponse | null>(null);

  const northValid = form.northHand.trim().length > 0;
  const southValid = form.southHand.trim().length > 0;
  const formValid = northValid && southValid;

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setTouched(true);

    if (!formValid) {
      return;
    }

    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const response = await bffApiClient.recommendBidding(form);
      setResult(response);
    } catch {
      setError('Request failed');
    } finally {
      setLoading(false);
    }
  }

  function reset() {
    setForm(initialForm);
    setTouched(false);
    setError(null);
    setResult(null);
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
                onChange={(event) => setForm({ ...form, northHand: event.target.value })}
              />
              {touched && !northValid ? <small className="field-error">North hand is required</small> : null}
            </label>

            <label className="field">
              <span>South</span>
              <textarea
                rows={4}
                value={form.southHand}
                onBlur={() => setTouched(true)}
                onChange={(event) => setForm({ ...form, southHand: event.target.value })}
              />
              {touched && !southValid ? <small className="field-error">South hand is required</small> : null}
            </label>

            <label className="field">
              <span>Bidding</span>
              <textarea
                rows={4}
                value={form.bidding}
                onChange={(event) => setForm({ ...form, bidding: event.target.value })}
              />
            </label>

            <label className="field">
              <span>System</span>
              <select
                value={form.system}
                onChange={(event) => setForm({ ...form, system: event.target.value as System })}
              >
                <option value="POLISH_CLUB">Polish Club</option>
                <option value="STANDARD_AMERICAN">Standard American</option>
              </select>
            </label>

            {error ? <div className="result-panel error-panel">{error}</div> : null}

            {result ? (
              <details className="result-panel" open>
                <summary>{result.bidding}</summary>
                <p>{result.explanation}</p>
              </details>
            ) : null}
          </div>

          <footer className="card-actions">
            <button className="primary-action" type="submit" disabled={loading || !formValid}>
              {loading ? 'Calculating...' : 'Calculate'}
            </button>
            <button className="secondary-action" type="button" onClick={reset}>
              Reset
            </button>
          </footer>
        </article>
      </form>
    </section>
  );
}
