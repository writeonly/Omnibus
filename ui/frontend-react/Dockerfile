# =========================
# Stage 1: dependencies
# =========================
FROM node:20-alpine AS deps

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci


# =========================
# Stage 2: build
# =========================
FROM node:20-alpine AS builder

WORKDIR /app

COPY --from=deps /app/node_modules ./node_modules

# copy only necessary files for better caching
COPY index.html ./
COPY tsconfig.json tsconfig.node.json vite.config.ts ./
COPY src ./src

RUN npm run build


# =========================
# Stage 3: runtime (nginx)
# =========================
FROM nginx:1.27-alpine AS runner

# remove default nginx static files
RUN rm -rf /usr/share/nginx/html/*

# copy build output
COPY --from=builder /app/dist /usr/share/nginx/html

# SPA fallback routing (React Router / Vite SPA)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# security hardening (optional but enterprise)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 5173

CMD ["nginx", "-g", "daemon off;"]
