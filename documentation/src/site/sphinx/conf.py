# -*- coding: utf-8 -*-
#
# Akka documentation build configuration file.
#

import sys, os

# -- General configuration -----------------------------------------------------

templates_path = ['_templates']
source_suffix = '.rst'
master_doc = 'index'
exclude_patterns = ['_build', 'pending', 'disabled']

project = u'OpenGSN'
copyright = u'2009-2011, Synchromedia, Inocybe Technologies Inc and Communications Research Centre'
version = '2.0-SNAPSHOT'
release = '2.0-SNAPSHOT'

#pygments_style = 'simple'
highlight_language = 'java'
add_function_parentheses = False
show_authors = True

# -- Options for HTML output ---------------------------------------------------

html_theme = 'gsn'
html_theme_path = ['_sphinx/themes']

html_title = 'OpenGSN Documentation'
html_logo = '_sphinx/static/logo.jpg'
#html_favicon = None

html_static_path = ['_sphinx/static']

html_last_updated_fmt = '%b %d, %Y'
#html_sidebars = {}
#html_additional_pages = {}
html_domain_indices = False
html_use_index = False
html_show_sourcelink = False
html_show_sphinx = False
html_show_copyright = True
htmlhelp_basename = 'OpenGSNdoc'

# -- Options for LaTeX output --------------------------------------------------

latex_paper_size = 'a4'
latex_font_size = '10pt'

latex_documents = [
  ('index', 'gsn.tex', u' OpenGSN Documentation',
   u'GreenStar Network', 'manual'),
]

latex_elements = {
    'classoptions': ',oneside,openany',
    'babel': '\\usepackage[english]{babel}',
    'preamble': '\\definecolor{VerbatimColor}{rgb}{0.935,0.935,0.935}'
    }

latex_logo = '_sphinx/static/logo.jpg'
