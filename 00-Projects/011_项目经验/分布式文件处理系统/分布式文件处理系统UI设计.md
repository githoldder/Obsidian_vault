Below is a **ready-to-paste UI/UX prompt** you can give directly to Gemini (or any design AI).  
It is written as a **product design brief + interface generation prompt**.

---

# Prompt: Design a Modern Web UI for a Distributed Cloud Drive + File Processing Platform

## Product Overview

Design a modern, clean, highly professional web application UI for a **Distributed File Processing Platform + Cloud Drive**.

This is NOT a normal cloud storage app.  
It is a **technical platform for distributed file processing powered by Hadoop/HDFS**.

Core product concept:

Upload → Store (HDFS) → Convert → Edit → Monitor distributed tasks

The UI must feel like:

- Enterprise SaaS
    
- Developer-friendly
    
- Infrastructure-aware
    
- Clean, modern, minimal
    
- Suitable for a graduation thesis demo
    

Target users:

- Developers
    
- Researchers
    
- Engineers
    
- Technical students
    

Design style inspiration:

- Vercel Dashboard
    
- AWS Console (simplified)
    
- Notion + Linear + Supabase
    
- Clean enterprise SaaS
    

Avoid consumer/cloud drive styles (Google Drive/iCloud).

---

# Global Layout

Use a **SaaS dashboard layout**.

Main layout structure:

Top Navigation Bar  
Left Sidebar Navigation  
Main Content Area

The layout must feel structured, data-heavy, and technical.

---

## Top Navigation Bar

Right side elements:

- User avatar + username
    
- Cluster status indicator (green / yellow / red)
    
- Active tasks counter
    

Left side:

- Product logo
    
- Workspace title: “Distributed File Processing Platform”
    

---

## Sidebar Navigation

Vertical left sidebar with icons + labels:

1. Dashboard
    
2. My Files
    
3. Convert Center
    
4. PDF Studio
    
5. Task Monitor
    
6. System Status
    

Use minimalist outline icons.

---

# Page 1 — Dashboard (System Overview)

This page must immediately communicate **distributed system capability**.

Sections:

### Storage Overview Card

Display:

- HDFS Total Capacity
    
- Used Storage
    
- Remaining Storage
    
- Total Files
    

Use progress bars + numbers.

### Cluster Nodes Status

Table or cards showing:

- Node name
    
- CPU usage
    
- Memory usage
    
- Status (online/offline)
    

### Daily Task Statistics

Simple chart:

- Conversions today
    
- PDF tasks today
    
- Failed tasks
    

Use modern analytics card style.

---

# Page 2 — My Files (Cloud Drive)

This page acts as the **HDFS file manager**.

Main components:

### Upload Area

Drag-and-drop upload zone at top.

### Breadcrumb Navigation

Show current folder path.

### File Table

Columns:

- File name
    
- Size
    
- Type
    
- Last modified
    
- Actions
    

Row actions:

- Download
    
- Delete
    
- Convert
    
- Open in PDF Studio (if PDF)
    

Right-click context menu is a plus.

Visual tone: developer cloud storage, not consumer style.

---

# Page 3 — Convert Center (Core Feature)

This is the **hero feature** of the product.

Layout sections:

### Upload / Select File Area

### Conversion Options Panel (dynamic form)

Supported conversions:

- DOC/DOCX → PDF
    
- PPT/PPTX → PDF
    
- Excel → PDF
    
- PDF → Images
    
- Images → PDF
    

When file selected, show options:

Document → PDF options:

- Preserve layout (High fidelity)
    
- Auto pagination
    
- Embed fonts
    

PDF → Images options:

- DPI selector (150 / 300 / 600)
    
- Output format (PNG / JPG)
    

### Submit Conversion Button

### Task List Table

Columns:

- Task ID
    
- File name
    
- Conversion type
    
- Status
    
- Progress bar
    

Statuses:  
Queued / Processing / Success / Failed

---

# Page 4 — PDF Studio

This page should feel like a **lightweight PDF editor workspace**.

Layout:  
Left: PDF Viewer  
Right: Tool Panel

Toolbar tools:

- Add text
    
- Add image
    
- Delete page
    
- Rotate page
    
- Merge PDF
    
- Split PDF
    
- Export
    

Visual tone: professional document tools.

---

# Page 5 — Task Monitor (Distributed System Showcase)

This page highlights **queue + workers + concurrency**.

Top metrics cards:

- Queue length
    
- Active workers
    
- Average processing time
    

Live updating task table:  
Columns:

- Task ID
    
- Task type
    
- Worker node
    
- Processing time
    
- Status
    

Color-coded statuses:  
Gray = queued  
Blue = processing  
Green = success  
Red = failed

This page must feel “real-time system”.

---

# Page 6 — System Status (Hadoop Integration)

Purpose: show infrastructure credibility.

Design two large panels:

- HDFS Web UI preview
    
- YARN Web UI preview
    

They can appear as embedded dashboards or framed previews.

This page should look very technical and infrastructure-focused.

---

# Visual Style Guidelines

Use:

- Light theme
    
- Neutral colors (white, gray, blue accents)
    
- Rounded cards
    
- Subtle shadows
    
- Spacious layout
    
- Modern SaaS typography
    

Keywords:  
clean, minimal, enterprise, technical, dashboard, modern SaaS.

Avoid:  
playful colors, cartoon style, consumer cloud aesthetics.

---

# Deliverables Expected from the Designer AI

Generate:

- Full dashboard UI mockups
    
- Sidebar navigation
    
- Each page layout
    
- Component-level detail
    
- Consistent design system
    

Focus on **MVP production-ready UI**.