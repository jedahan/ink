# {{name}}

A quil sketch, made with [ink](http://jedahan.com/ink)

The default sketch has a concept of time for animations. You can press 'd' to toggle debug view, 's' to save a png, and 'q' to quit.

## develop

    boot develop

Will start a server at [http://localhost:3000](), which will live-reload as you make changes to the sketch

## build

    boot build

Will create a main.js and assets, with speed optimizations on

## deploy

    boot github-pages

Does the same thing as `boot build`, but writes it out to [docs/]() so you can enable github pages on the docs directory to quickly serve your sketch
