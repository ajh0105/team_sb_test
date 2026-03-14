// Shared navigation script
(function() {
  // Hamburger toggle
  const hamburger = document.getElementById('hamburger');
  const sidebar = document.getElementById('sidebar');
  const overlay = document.getElementById('overlay');

  function openSidebar() {
    sidebar && sidebar.classList.add('open');
    overlay && overlay.classList.add('show');
  }
  function closeSidebar() {
    sidebar && sidebar.classList.remove('open');
    overlay && overlay.classList.remove('show');
  }

  hamburger && hamburger.addEventListener('click', openSidebar);
  overlay && overlay.addEventListener('click', closeSidebar);

  // Highlight active nav link
  const currentPath = window.location.pathname.split('/').pop() || 'index.html';
  document.querySelectorAll('.sidebar-nav a').forEach(link => {
    const href = link.getAttribute('href');
    if (href && (href === currentPath || href.endsWith('/' + currentPath))) {
      link.classList.add('active');
    }
  });

  // Keyboard shortcut: Escape to close sidebar
  document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') closeSidebar();
  });
})();
