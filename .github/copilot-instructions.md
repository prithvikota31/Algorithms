# Repository instructions

## Keep local work and upstream synchronized

The user has authorized automatic commits and pushes for assistant changes in this repository.
Synchronizing the current branch with its configured upstream is part of completing a task.
Apply this to new files and folders, edits, renames, and deletions unless the user explicitly requests otherwise.
These instructions govern assistant work; they do not install a background filesystem watcher.

1. Check the working tree, staged changes, current branch, and configured upstream before editing.
   Fetch the upstream remote and fast-forward when needed and safe.
   Preserve unrelated changes and existing staged work.
2. Git tracks files, not empty directories.
   Add an empty `.gitkeep` inside a requested directory if it has no other tracked content.
   Do not add placeholders to ignored or generated directories.
3. Finish and validate a coherent batch of changes before publishing it.
   During interactive lessons, synchronize a completed file-changing step before waiting for the learner.
   Do not push unfinished code or changes that fail applicable checks.
4. Check the intended changes for secrets, private data, ignored artifacts, and accidental additions.
   Stage only explicit task-related paths, never a blanket `git add .` or `git add -A`.
   Ensure a commit will not include unrelated pre-existing staged changes.
   Commit the validated changes with a descriptive message.
5. Push to the current branch's configured upstream remote and branch.
   Do not assume the remote is named `origin` or the branch is named `main`.
   Do not change the destination or create an upstream without confirming the intended target.
6. Verify the local commit matches the remote branch tip and that ahead/behind counts are both zero.
   Check for remaining staged, modified, or untracked task files.
   Report the commit, destination, and any unrelated local changes instead of claiming a fully clean tree.

If authentication, a rejected push, divergent history, or a conflict prevents synchronization, report the blocker.
Never force-push, discard changes, rewrite existing commits, or bypass hooks to make the branches match.
Do not claim synchronization succeeded until the remote state has been verified.
