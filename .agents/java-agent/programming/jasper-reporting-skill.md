# Jasper Reporting Skill

## Purpose

Generate and export reports using **JasperReports 7.x** via the platform **Reporting Engine** (`edx-reporting-engine`).

Use for PDF, HTML, Excel, DOCX, and other supported export formats — not for simple paginated REST list APIs (use projections instead).

## Platform Module

| Module | Package | Role |
|--------|---------|------|
| `edx-reporting-engine` | `com.swifttech.edx.re` | Jasper engine, format generators, templates |

## Dependencies (reference)

```gradle
implementation 'net.sf.jasperreports:jasperreports:7.0.3'
implementation 'net.sf.jasperreports:jasperreports-pdf:7.0.0'
implementation 'net.sf.jasperreports:jasperreports-jdt:7.0.0'
implementation 'net.sf.jasperreports:jasperreports-fonts:7.0.3'
```

## Architecture Pattern

```
ReportRequest → ReportEngine (JasperReportEngine)
    → load .jrxml / compiled .jasper template
    → JasperFillManager.fillReport(parameters, dataSource)
    → BaseJasperReportGenerator (per format via JasperFormatGeneratorFactory)
    → OutputStream (PDF / HTML / Excel / DOCX)
```

## Package Layout (reporting module)

```
com.swifttech.edx.re
├── service/
│   ├── ReportEngine.java
│   ├── BaseJasperReportGenerator.java
│   └── impl/jasper/
│       ├── JasperReportEngine.java
│       ├── PdfJasperReportGenerator.java
│       ├── HtmlJasperReportGenerator.java
│       └── ...
├── factory/
│   └── JasperFormatGeneratorFactory.java
├── enums/
│   └── ReportFormatEnum.java
└── resources/reports/          # .jrxml / .jasper templates
```

## Rules

1. **Product / reporting module** owns `ReportEngine` interface and `JasperReportEngine` implementation
2. One **format generator** per export type implementing `BaseJasperReportGenerator`
3. Register generators in `JasperFormatGeneratorFactory` — do not hardcode format switches in engine
4. Store templates under `src/main/resources/reports/` (or configured `templatePath`)
5. Pass report parameters as `Map<String, Object>`; use `JRBeanCollectionDataSource` for collection data
6. Large reports: prefer async job + download endpoint (poll `GlobalResponse` status)
7. Map failures to `GlobalException` with message codes from [message-code-convention-skill.md](message-code-convention-skill.md)

## Report Flow (service layer)

```java
public void generateReport(ReportRequest request, OutputStream outputStream) {
    validateRequest(request);
    JasperReport template = loadTemplate(request.getTemplateName());
    JasperPrint print = JasperFillManager.fillReport(
        template,
        request.getParameters(),
        getDataSource(request)
    );
    JasperFormatGeneratorFactory.getGenerator(request.getReportFormat())
        .createReport(print, outputStream);
}
```

## Data Sources

| Source | Use |
|--------|-----|
| `JRBeanCollectionDataSource` | In-memory collection from service/query |
| `JREmptyDataSource` | Parameter-only reports |
| JDBC / JPA query result | Export via collection or dedicated query in reporting service |

Prefer fetching data in application service, then passing a collection — keep Jasper templates free of raw SQL when possible.

## Smart Remittance Placement

| Concern | Module |
|---------|--------|
| Generic report engine | `edx-reporting-engine` (platform) |
| Remittance report templates & parameters | `sr-reporting-services` (product) |
| Tenant-specific template/branding | Solution module |

## API Integration

- Expose report generation via REST in `sr-reporting-services`
- Return file download or async job ID wrapped in `GlobalResponse`
- Document export format in OpenAPI (`application/pdf`, etc.)

## Anti-Patterns

| Pattern | Why |
|---------|-----|
| Jasper logic in REST controller | Belongs in reporting service |
| New format without factory registration | Breaks `supportsFormat()` contract |
| Full entity graphs as report data | Map to report DTOs first |
| Synchronous multi-MB reports | Use async export job |

## Related

- [performance-skill.md](performance-skill.md)
- [exception-handling-skill.md](exception-handling-skill.md)
- [../module-map.md](../module-map.md) — `sr-reporting-services`
