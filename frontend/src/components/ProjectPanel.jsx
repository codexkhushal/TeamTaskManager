import { useState } from "react";

const initialForm = { name: "", description: "", deadline: "", memberIds: [] };

export default function ProjectPanel({ projects, users, isAdmin, onCreateProject, onDeleteProject }) {
  const [form, setForm] = useState(initialForm);

  function toggleMember(memberId) {
    setForm((current) => ({
      ...current,
      memberIds: current.memberIds.includes(memberId)
        ? current.memberIds.filter((id) => id !== memberId)
        : [...current.memberIds, memberId]
    }));
  }

  async function submit(event) {
    event.preventDefault();
    await onCreateProject({
      ...form,
      deadline: form.deadline || null
    });
    setForm(initialForm);
  }

  return (
    <section className="panel-grid">
      <div className="panel">
        <div className="panel-header">
          <h2>Projects</h2>
          <p>{projects.length} active spaces</p>
        </div>
        <div className="project-list">
          {projects.map((project) => (
            <article className="project-card" key={project.id}>
              <div className="project-card-header">
                <h3>{project.name}</h3>
                {isAdmin ? (
                  <button className="inline-action" onClick={() => onDeleteProject(project.id)} type="button">
                    Delete
                  </button>
                ) : null}
              </div>
              <div>
                <p>{project.description || "No description yet."}</p>
              </div>
              <div className="project-meta">
                <span>Owner: {project.owner.name}</span>
                <span>Deadline: {project.deadline || "Flexible"}</span>
                <span>Members: {project.members.length}</span>
              </div>
            </article>
          ))}
        </div>
      </div>

      {isAdmin ? (
        <form className="panel form-panel" onSubmit={submit}>
          <div className="panel-header">
            <h2>Create project</h2>
            <p>Set the team up with a clear delivery space.</p>
          </div>
          <label>
            Project name
            <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
          </label>
          <label>
            Description
            <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          </label>
          <label>
            Deadline
            <input type="date" value={form.deadline} onChange={(e) => setForm({ ...form, deadline: e.target.value })} />
          </label>
          <div>
            <span className="field-label">Team members</span>
            <div className="member-pills">
              {users.map((user) => (
                <label className="member-pill" key={user.id}>
                  <input
                    checked={form.memberIds.includes(user.id)}
                    onChange={() => toggleMember(user.id)}
                    type="checkbox"
                  />
                  <span>{user.name}</span>
                </label>
              ))}
            </div>
            <p className="helper-text">Select every teammate who should be eligible for tasks in this project.</p>
          </div>
          <button className="primary-button" type="submit">Create project</button>
        </form>
      ) : null}
    </section>
  );
}
