import { Injectable, OnModuleDestroy } from '@nestjs/common';
import { ClientHttp2Session, connect } from 'http2';

@Injectable()
export class GrpcTransport implements OnModuleDestroy {
  private readonly sessions = new Map<string, ClientHttp2Session>();

  async unary(target: string, path: string, payload: Uint8Array): Promise<Uint8Array> {
    const session = this.sessionFor(target);
    const request = session.request({
      ':method': 'POST',
      ':path': path,
      'content-type': 'application/grpc+proto',
      te: 'trailers',
    });

    const responseChunks: Buffer[] = [];

    return new Promise((resolve, reject) => {
      request.on('data', (chunk: Buffer) => responseChunks.push(chunk));
      request.on('error', reject);
      request.on('end', () => {
        try {
          const response = Buffer.concat(responseChunks);
          resolve(readGrpcFrame(response));
        } catch (error) {
          reject(error);
        }
      });

      request.end(writeGrpcFrame(payload));
    });
  }

  onModuleDestroy(): void {
    for (const session of this.sessions.values()) {
      session.close();
    }
  }

  private sessionFor(target: string): ClientHttp2Session {
    const normalized = target.startsWith('http') ? target : `http://${target}`;
    const current = this.sessions.get(normalized);

    if (current && !current.closed && !current.destroyed) {
      return current;
    }

    const session = connect(normalized);
    this.sessions.set(normalized, session);
    return session;
  }
}

function writeGrpcFrame(payload: Uint8Array): Buffer {
  const frame = Buffer.alloc(5 + payload.length);
  frame.writeUInt8(0, 0);
  frame.writeUInt32BE(payload.length, 1);
  Buffer.from(payload).copy(frame, 5);
  return frame;
}

function readGrpcFrame(buffer: Buffer): Uint8Array {
  if (buffer.length < 5) {
    return new Uint8Array();
  }

  const compressed = buffer.readUInt8(0);
  if (compressed !== 0) {
    throw new Error('Compressed gRPC responses are not supported');
  }

  const length = buffer.readUInt32BE(1);
  return buffer.subarray(5, 5 + length);
}
