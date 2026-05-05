import { useState } from 'react';
import type { FormEvent } from 'react';

import { bffApiClient } from '../../core/api/bffApiClient';
import type { NextBidResponse, System } from '../../core/api/bffApiClient';

interface NextBidForm {
  hand: string;
  bidding: string;
  system: System;
}

const initialForm: NextBidForm = {
  hand: '',
  bidding: '',
  system: 'POLISH_CLUB'
};

export function NextBid() {
  const [form, setForm] = useState<NextBidForm>(initialForm);
  const [touched, setTouched] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<NextBidResponse | null>(null);

  const formValid = form.hand.trim().length > 0;

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
      const response = await bffApiClient.recommendBid(form);
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
                onChange={(event) => setForm({ ...form, hand: event.target.value })}
              />
              {touched && !formValid ? <small className="field-error">Hand is required</small> : null}
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
                <summary>{result.bid}</summary>
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
