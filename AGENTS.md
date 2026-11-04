# Working instructions

- Whenever the user returns with a project question or task, first read the latest relevant chat between the user and the Antigravity agent directly from Antigravity's local conversation storage. Check for new messages and any newer relevant conversation; do not rely only on the saved export or this chat's earlier checkpoint. Establish what the user has done with the other agent, then compare it with current project files before answering or editing. If the latest chat cannot be read, say so rather than assume no progress.
- Keep explanations clear, concise, and direct. The user already knows Spring Boot CRUD, JPA transactions, and basic Kafka/Redis. Focus on the new concept being asked about.
- Before answering course-specific questions or implementing course work, inspect the relevant transcript chapter. When the transcript does not show enough detail, capture and visually inspect the instructor's video at the relevant timestamp, and check the instructor's source code. Explain uncertainties instead of guessing what the instructor did.
- In this project's course context, “computer use” can mean inspecting a video frame; desktop automation is not required for that.
- Follow the instructor's core architecture and logic while preserving the user's existing naming, packages, folder layout, and deliberate differences. Inspect existing files before creating anything with the same responsibility.
- Implement application changes when the user asks for those changes. A question, explanation request, or review alone does not authorize code edits; describe any proposed edits first.
- Do not commit or push unless explicitly instructed. Commit-message convention: `feat(<service>): <summary>` for features, with the appropriate type for other changes.
- When implementing a plan, do not use sub-agents. Do not write tests unless the user explicitly requests them.
- Preserve pre-existing local changes. Check current files and Git status rather than assuming the other agent's recap is accurate.

# Course resources

Prepared reference folder:
`/Users/youssefmohamed/Documents/Codex/2026-11-04/rge/work/patient-management-resources`

Read `RESOURCE_GUIDE.md` there for resource locations, commands, provenance, and limitations. Read `PROJECT_CONTEXT.md` for the preparation-time snapshot; recheck live project files before acting.

- `transcript.txt`: timestamped course captions.
- `raw_transcript.json`: individual timed caption snippets.
- `course_transcript_by_chapter.json`: 86 chapter entries with captions.
- `chapters.json`: compact chapter index. Match the lesson number in `name`, not the array index; the course includes chapters 4a and 68a.
- `transcript.db` and `search.py`: searchable local transcript.
- `reference-repo/`: independent local clone of the instructor's finished repository. Later-course features in this clone are not automatically in scope for the current lesson.
- `antigravity-messages.json` and `.md`: saved user/agent message text exported from the prior conversation's local search index.
- Live Antigravity sources: `/Users/youssefmohamed/.gemini/antigravity/conversation_summaries.db` and `/Users/youssefmohamed/.gemini/antigravity/conversations/`. The original project conversation ID is `608a6bbb-e519-4d76-ac00-2147dc98d1eb`; check the index for newer relevant conversations as well.
- `capture_frame.py` and `frames/`: video-frame extraction and captured evidence. See the resource guide for the Python interpreter it requires.

Course video: https://www.youtube.com/watch?v=tseqdcFfTUY
Instructor repository: https://github.com/chrisblakely01/java-spring-microservices

At preparation on 2026-11-04, the user's last course checkpoint was after Lesson 20; Lesson 21 (gRPC introduction) starts at 03:28:51. Recheck progress when resuming later.
