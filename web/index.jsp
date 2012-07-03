<%-- 
    Document   : index
    Created on : Apr 28, 2011, 9:49:11 AM
    Author     : mcculley
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ortelium Symbol Factory</title>
        <link href="style.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <h1 id="title">Ortelium Symbol Factory</h1>
        <p>Ortelium generates map symbols. It currently has prototype support for <a href="http://en.wikipedia.org/wiki/2525B">2525B</a>.</p>
        <p>Ortelium is a product of <a href="http://www.stackframe.com/software/Ortelium">StackFrame, LLC</a>.</p>
        <form action="symbol" method="GET">
            <label for="code"><abbr title="Symbol Identification Code">SIDC</abbr>:</label>
            <input type="text" name="SIDC" id="code" size="15" value="SNGPUCFSA-*****"><br>
            <label for="quantity">Quantity:</label>
            <input type="text" name="C" id="quantity" size="3" value=""><br>
            <label for="outputType">Image Format:</label>
            <select id="outputType" name="outputType">
                <c:forEach var="option" items="${outputTypes}">
                    <option value="${option.value.mimeType}">${option.value.name}</option>
                </c:forEach>
            </select>
            <br>
            <label for="spec">Specification:</label>
            <select id="spec" name="spec">
                <c:forEach var="entry" items="${specifications}">
                    <option value="${entry.key}">${entry.key}</option>
                </c:forEach>
            </select>
            <br>            
<!--
            <label for="width">Width:</label>
            <input type="text" name="width" id="width" size="4" value="">
            <label for="height">Height:</label>
            <input type="text" name="height" id="height" size="4" value="">
            <br>
-->
            <input type="submit" value="Create">
        </form>
        <h2>Examples</h2>
        <ul>
            <li>
                <c:url var="link" value="symbol">
                    <c:param name="SIDC" value="SHGPUCAWS-*****"/>
                    <c:param name="outputType" value="image/svg+xml"/>
                    <c:param name="spec" value="2525B"/>
                </c:url>
                <a href="${link}">Hostile Armored Wheeled Air Assault (no echelon)</a>
            </li>
            <li>
                <c:url var="link" value="symbol">
                    <c:param name="SIDC" value="SHGPUCAWS--D***"/>
                    <c:param name="outputType" value="image/svg+xml"/>
                    <c:param name="spec" value="2525B"/>
                </c:url>
                <a href="${link}">Hostile Armored Wheeled Air Assault (platoon)</a>
            </li>
            <li>
                <c:url var="link" value="symbol">
                    <c:param name="SIDC" value="SFGPUCAWS-*****"/>
                    <c:param name="outputType" value="image/svg+xml"/>
                    <c:param name="spec" value="2525B"/>
                </c:url>
                <a href="${link}">Friendly Armored Wheeled Air Assault</a>
            </li>
            <li>
                <c:url var="link" value="symbol">
                    <c:param name="SIDC" value="SFGPEWRH--MT***"/>
                    <c:param name="C" value="200"/>
                    <c:param name="outputType" value="image/svg+xml"/>
                    <c:param name="spec" value="2525B"/>
                </c:url>
                <a href="${link}">Machine Gun with quantity 200</a>
            </li>
        </ul>

        <p><a href="symbols">All Symbols</a> (Warning: This will show ${fn:length(symbolRepository.codes)} symbols and may make your browser unhappy.)</p>

        <p>Trouble? Suggestions? Contact <a href="mailto:support@stackframe.com">support@stackframe.com</a>.</p>
        <p>Ortelium is &copy; 2011-2012 StackFrame, LLC and is licensed under <a href="gpl-2.0-standalone.html">version 2 of the General Public License</a>.</p>
    </body>
</html>
