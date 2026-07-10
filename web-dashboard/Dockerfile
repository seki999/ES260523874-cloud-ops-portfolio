FROM node:20-alpine

WORKDIR /app

COPY package.json package-lock.json vite.config.js ./
COPY web-dashboard ./web-dashboard

RUN npm ci

EXPOSE 5173

CMD ["npm", "run", "dev:docker"]
