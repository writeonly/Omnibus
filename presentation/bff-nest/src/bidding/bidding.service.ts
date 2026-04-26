import { Injectable, BadGatewayException } from '@nestjs/common';

@Injectable()
export class BiddingService {
  private readonly backendBaseUrl =
    process.env.BIDDING_ENGINE_BASE_URL ?? 'http://localhost:8081';

  async recommend(request: any) {
    const payload = {
      northHand: request.northHand?.trim(),
      southHand: request.southHand?.trim(),
      auction: request.auction?.trim() ?? '',
      system: request.system?.trim() ?? 'DEFAULT_SYSTEM',
    };

    const url = `${this.backendBaseUrl}/api/v1/bidding/recommend`;

    try {
      console.log('[BIDDING] calling:', url);
      console.log('[BIDDING] payload:', JSON.stringify(payload));

      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const body = await response.text();

        throw new BadGatewayException({
          message: 'Bidding backend error',
          status: response.status,
          url,
          details: body,
          payloadSent: payload,
        });
      }

      return await response.json();
    } catch (err: any) {
      console.error('[BIDDING] FETCH FAILED ❌');
      console.error('URL:', url);
      console.error('ERROR NAME:', err?.name);
      console.error('ERROR MESSAGE:', err?.message);
      console.error('CAUSE:', err?.cause);

      throw new BadGatewayException({
        message: 'Fetch failed to backend',
        url,
        error: err?.message,
        cause: err?.cause,
        payloadSent: payload,
      });
    }
  }
}
