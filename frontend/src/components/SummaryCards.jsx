export default function SummaryCards({ summary }) {
  const cards = [
    { label: "Total tasks", value: summary.totalTasks || 0 },
    { label: "Completed", value: summary.completedTasks || 0 },
    { label: "Pending", value: summary.pendingTasks || 0 },
    { label: "Overdue", value: summary.overdueTasks || 0 }
  ];

  return (
    <section className="summary-grid">
      {cards.map((card) => (
        <article className="summary-card" key={card.label}>
          <span>{card.label}</span>
          <strong>{card.value}</strong>
        </article>
      ))}
    </section>
  );
}
