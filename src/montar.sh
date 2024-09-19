javac phantom/main/Main.java
javac phantom/main/Fix.java
javac phantom/main/List.java
jar cvfm phantom.jar phantom.mf phantom/* toolbox/*
jar cvfm fix.jar fix.mf phantom/* toolbox/*
jar cvfm list.jar list.mf phantom/* toolbox/*
