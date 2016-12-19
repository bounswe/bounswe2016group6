# Annotorious Semantic Tagging Plugin

A plugin to [Annotorious](http://annotorious.github.io) which adds __Semantic Tagging__
functionality: while typing the annotation, text is sent to the server for
[Named Entity Recognition](http://en.wikipedia.org/wiki/Named-entity_recognition).
Recognized entities are suggested as possible tags, and the user can add them to the
annotation by clicking on them (see screenshot). Tags are _Semantic Tags_ in the sense that
they are not only strings, but (underneath the hood) include a link to their corresponding
[DBpedia](http://dbpedia.org) entry.

![Semantic Tagging Screenshot](http://github.com/annotorious/annotorious-semantic-tagging-plugin/raw/master/semantic-tagging-screenshot.jpg "Semantic Tagging Screenshot")

## Live Demo

A live demo of the plugin is available [here](http://annotorious.github.io/demos/semantic-tagging-preview.html).
Beware: beta!!

## Client

The client library relies on [jQuery](http://jquery.com)

## TODO

There are several TODOs in the code. [Get in touch](http://groups.google.com/group/annotorious) if you want to
contribute!

__TODO__ @behas: add server side Python NER/DBpedia linking scripts

## License

The Annotorious Semantic Tagging Plugin is licensed under the Apache 2 License.



