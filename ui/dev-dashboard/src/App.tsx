import React from "react";

const services = [
  { name: "Angular", url: "/angular", color: "bg-red-500" },
  { name: "React", url: "/react", color: "bg-blue-500" },
  { name: "BFF API", url: "/api", color: "bg-green-500" },
  { name: "Grafana", url: "/grafana", color: "bg-orange-500" },
  { name: "Keycloak", url: "/keycloak", color: "bg-purple-500" },
  { name: "Kafka UI", url: "/kafka", color: "bg-yellow-500" },
];

export default function DevDashboard() {
  return (
    <div className="min-h-screen bg-slate-900 text-white p-8">
      <h1 className="text-3xl font-bold mb-8">🚀 Omnibus Platform</h1>

      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {services.map((svc) => (
          <a
            key={svc.name}
            href={svc.url}
            className={`rounded-2xl p-6 shadow-lg hover:scale-105 transition ${svc.color}`}
          >
            <h2 className="text-xl font-semibold">{svc.name}</h2>
            <p className="text-sm opacity-80 mt-2">Open service</p>
          </a>
        ))}
      </div>

      <div className="mt-12">
        <h2 className="text-xl mb-4">System Status</h2>
        <ul className="space-y-2">
          <li>Postgres: 🟢</li>
          <li>Kafka: 🟢</li>
          <li>Cassandra: 🟢</li>
          <li>Keycloak: 🟢</li>
        </ul>
      </div>
    </div>
  );
}
