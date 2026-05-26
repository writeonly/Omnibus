# Stage 1: deps
FROM node:20-alpine AS deps
WORKDIR /app

COPY package*.json ./
RUN npm ci


# Stage 2: build
FROM node:20-alpine AS builder
WORKDIR /app

COPY --from=deps /app/node_modules ./node_modules
COPY . .

RUN npm run build


# Stage 3: runtime (minimal server)
FROM node:20-alpine AS runner
WORKDIR /app

ENV NODE_ENV=production

# Next standalone output
COPY --from=builder /app/.next/standalone ./
COPY --from=builder /app/.next/static ./.next/static
COPY --from=builder /app/public ./public

EXPOSE 5174

CMD ["node", "server.js"]
