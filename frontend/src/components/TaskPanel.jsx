import { useState } from "react";

const initialTask = {
  title: "",
  description: "",
  projectId: "",
  assigneeId: "",
  deadline: "",
  status: "TODO"
};

const statuses = ["TODO", "IN_PROGRESS", "DONE"];

export default function TaskPanel({ tasks, projects, users, isAdmin, onCreateTask, onUpdateTaskStatus }) {
  const [form, setForm] = useState(initialTask);

  async function submit(event) {
    event.preventDefault();
    await onCreateTask({
      ...form,
      projectId: Number(form.projectId),
      assigneeId: Number(form.assigneeId),
      deadline: form.deadline || null
    });
    setForm(initialTask);
  }

  return (
    <section className="panel-grid">
      <div className="panel">
        <div className="panel-header">
          <h2>Tasks</h2>
          <p>Track movement from idea to done.</p>
        </div>
        <div className="task-list">
          {tasks.map((task) => (
            <article className="task-card" key={task.id}>
              <div className="task-card-top">
                <div>
                  <h3>{task.title}</h3>
                  <p>{task.description || "No task notes."}</p>
                </div>
                <select
                  value={task.status}
                  onChange={(event) => onUpdateTaskStatus(task.id, event.target.value)}
                >
                  {statuses.map((status) => (
                    <option key={status} value={status}>{status.replace("_", " ")}</option>
                  ))}
                </select>
              </div>
              <div className="task-meta">
                <span>{task.projectName}</span>
                <span>{task.assignee.name}</span>
                <span>{task.deadline || "No deadline"}</span>
              </div>
            </article>
          ))}
        </div>
      </div>

      {isAdmin ? (
        <form className="panel form-panel" onSubmit={submit}>
          <div className="panel-header">
            <h2>Assign task</h2>
            <p>Keep accountability clear and deadlines visible.</p>
          </div>
          <label>
            Task title
            <input value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} required />
          </label>
          <label>
            Description
            <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          </label>
          <label>
            Project
            <select value={form.projectId} onChange={(e) => setForm({ ...form, projectId: e.target.value })} required>
              <option value="">Select project</option>
              {projects.map((project) => (
                <option key={project.id} value={project.id}>{project.name}</option>
              ))}
            </select>
          </label>
          <label>
            Assignee
            <select value={form.assigneeId} onChange={(e) => setForm({ ...form, assigneeId: e.target.value })} required>
              <option value="">Select teammate</option>
              {users.map((user) => (
                <option key={user.id} value={user.id}>{user.name}</option>
              ))}
            </select>
          </label>
          <label>
            Deadline
            <input type="date" value={form.deadline} onChange={(e) => setForm({ ...form, deadline: e.target.value })} />
          </label>
          <button className="primary-button" type="submit">Assign task</button>
        </form>
      ) : null}
    </section>
  );
}
