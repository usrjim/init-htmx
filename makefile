dev:
	clojure -M:dev:nrepl

rebl:
	clojure -M:dev:rebl:nrepl -m nrepl.cmdline --middleware '[nrepl-rebl.core/wrap-rebl]'

run:
	clojure -M -m usrj.htmx

uber:
	clojure -T:build uber

clean:
	clojure -T:build clean
