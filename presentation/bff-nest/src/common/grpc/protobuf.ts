export type ProtobufValue = string | number | boolean | Uint8Array;
export type ProtobufFields = Map<number, ProtobufValue[]>;

export function encodeStringField(fieldNumber: number, value?: string): Uint8Array {
  if (!value) {
    return new Uint8Array();
  }

  const bytes = new TextEncoder().encode(value);
  return concat([encodeVarint((fieldNumber << 3) | 2), encodeVarint(bytes.length), bytes]);
}

export function encodeInt32Field(fieldNumber: number, value: number): Uint8Array {
  return concat([encodeVarint((fieldNumber << 3) | 0), encodeVarint(value)]);
}

export function encodeBoolField(fieldNumber: number, value: boolean): Uint8Array {
  return concat([encodeVarint((fieldNumber << 3) | 0), encodeVarint(value ? 1 : 0)]);
}

export function encodeMessageField(fieldNumber: number, value: Uint8Array): Uint8Array {
  return concat([encodeVarint((fieldNumber << 3) | 2), encodeVarint(value.length), value]);
}

export function decodeFields(buffer: Uint8Array): ProtobufFields {
  const fields: ProtobufFields = new Map();
  let offset = 0;

  while (offset < buffer.length) {
    const tag = readVarint(buffer, offset);
    offset = tag.offset;

    const fieldNumber = tag.value >> 3;
    const wireType = tag.value & 7;
    let value: ProtobufValue;

    if (wireType === 0) {
      const decoded = readVarint(buffer, offset);
      value = decoded.value;
      offset = decoded.offset;
    } else if (wireType === 2) {
      const length = readVarint(buffer, offset);
      offset = length.offset;
      value = buffer.slice(offset, offset + length.value);
      offset += length.value;
    } else {
      throw new Error(`Unsupported protobuf wire type ${wireType}`);
    }

    const values = fields.get(fieldNumber) ?? [];
    values.push(value);
    fields.set(fieldNumber, values);
  }

  return fields;
}

export function stringField(fields: ProtobufFields, fieldNumber: number): string {
  const value = fields.get(fieldNumber)?.[0];

  if (value instanceof Uint8Array) {
    return new TextDecoder().decode(value);
  }

  return '';
}

export function intField(fields: ProtobufFields, fieldNumber: number): number {
  const value = fields.get(fieldNumber)?.[0];
  return typeof value === 'number' ? value : 0;
}

export function messageFields(fields: ProtobufFields, fieldNumber: number): Uint8Array[] {
  return (fields.get(fieldNumber) ?? []).filter(
    (value): value is Uint8Array => value instanceof Uint8Array
  );
}

export function concat(parts: Uint8Array[]): Uint8Array {
  const length = parts.reduce((sum, part) => sum + part.length, 0);
  const result = new Uint8Array(length);
  let offset = 0;

  for (const part of parts) {
    result.set(part, offset);
    offset += part.length;
  }

  return result;
}

function encodeVarint(value: number): Uint8Array {
  const bytes: number[] = [];
  let next = value >>> 0;

  while (next > 127) {
    bytes.push((next & 127) | 128);
    next >>>= 7;
  }

  bytes.push(next);
  return Uint8Array.from(bytes);
}

function readVarint(buffer: Uint8Array, start: number): { value: number; offset: number } {
  let result = 0;
  let shift = 0;
  let offset = start;

  while (offset < buffer.length) {
    const byte = buffer[offset++];
    result |= (byte & 127) << shift;

    if ((byte & 128) === 0) {
      return { value: result, offset };
    }

    shift += 7;
  }

  throw new Error('Malformed protobuf varint');
}
