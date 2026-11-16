const legacyHeaders = new Set([
  'chore(docs,ui): update documentation, restore professional colors, fix dark mode, and resolve favorites layout stacking',
  'feat(profile,ui): simplify user profile, add preferred chapter language grouping, and modernize Material 3 dark/light theme',
  'Fix layout preview PhotoView, optimize reader page loading, implement intelligent chapter deduplication with prefix and language filtering, and update documentation',
  'fix: Add IDE and editor directories to .gitignore',
  'feat: agregar lector de manga, filtros e historial',
]);

module.exports = {
  extends: ['@commitlint/config-conventional'],
  ignores: [(commit) => legacyHeaders.has(commit.split('\n')[0])],
};
