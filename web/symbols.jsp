<%--
Copyright (C) 2011-2012 StackFrame, LLC

This program is free software; you can redistribute it and/or modify it under
the terms of version 2 of the GNU General Public License as published by the
Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. For all other purposes, contact sales@stackframe.com for a license.

You should have received a copy of version 2 of the GNU General Public
License along with this program; if not, contact info@stackframe.com.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>All Symbols</title>
    </head>
    <body>
        <table>
            <caption>All Symbols</caption>
            <thead>
                <tr><th>Code</th><th>Symbol</th><th>Description</th></tr>
            </thead>
            <tbody>
                <c:forEach var="code" items="${symbolRepository.codes}">
                    <tr>
                        <c:url var="link" value="symbol">
                            <c:param name="SIDC" value="${code}"/>
                            <c:param name="outputType" value="image/svg+xml"/>
                            <c:param name="spec" value="2525B"/>
                        </c:url>
                        <td> 
                            <a href="${link}">${code}</a>
                        </td>
                        <td>
                            <c:url var="thumbnail" value="symbol">
                                <c:param name="SIDC" value="${code}"/>
                                <c:param name="outputType" value="image/png"/>
                                <c:param name="spec" value="2525B"/>
                            </c:url>
                            <a href="${link}"><img width="50" height="50" src="${thumbnail}"/></a>
                        </td>
                        <td>${symbolRepository.codeDescriptions[code]}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
