<!DOCTYPE html>
<html lang="en">
<head>
    <title>Demo Spring Boot FreeMarker</title>
</head>
<body>
<table border = "1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Quantity</th>
    </tr>
    </thead>
    <tbody>
    <#list products as item>
        <tr>
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${item.quantity}</td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
