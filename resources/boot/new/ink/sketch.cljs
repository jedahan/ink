(ns {{name}}.sketch
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

; the setup function run once, and returns the initial state
(defn setup []
  (q/frame-rate 30) ; target 30 frames per second
  {:radius 10
   :hue 0
   :min-radius 25
   :center [(/ (q/width) 2) (/ (q/height) 2)]
   :debug true
  })

; mouse-moved runs every time the mouse is moved
; and gets passed an object with the current mouse x and y, and previous mouse x and y
(defn mouse-moved [state mouse]
  (let [radius (max (* 0.8 (:y mouse)) (:min-radius state))
        hue (q/map-range (:x mouse) 0 (q/height) 0 255)]
    (assoc state :radius radius :hue hue)
  ))

; key-pressed for debug mode, and saving graphics
(defn key-pressed [state event]
  (let [key (:key event)]
    (cond
      (= :esc key) (q/exit)
      (= :q key) (q/exit)
      (= :d key) (assoc state :debug (not (:debug state)))
      (= :s key) (assoc state :save-frame true)
      :else (do (println (str event)) state))))

; update-state runs before every frame
(defn update-state [state]
  (let [t (/ (q/frame-count) (q/target-frame-rate)) ]
    (if (state :save-frame) (q/save-frame "{{name}}-####.png"))
    (assoc state :time t :save-frame false)))

(defn draw-state [state]
  (q/color-mode :rgb)
  (q/background 0 140 255)
  (q/no-stroke)
  (q/color-mode :hsb)
  (q/fill (:hue state) 200 200)
  (q/with-translation (:center state)
    (q/ellipse (q/sin (:time state)) (q/cos (:time state)) (:radius state) (:radius state))
  )
  (if (:debug state)
    (let [text-size 20
          line-height (* 1.25 text-size)
          x 0
          y (- (q/height) text-size)]
      (q/fill 0 0 255)
      (q/text-size text-size)
      (q/text-align :right)
      (q/text "'d' toggles debug
              's' screenshots
              'q' quits" (- (q/width) 10) (- y 10 (* 2 line-height)))
      (q/text-align :left)
      (let [newlines (clojure.string/replace (str (assoc state :time (q/floor (:time state)))) "," "\n" )
            no-brackets (clojure.string/replace newlines "}" "")
            state-info (clojure.string/replace no-brackets "{" " ")]
        (q/text state-info 0 line-height)))))

(q/defsketch {{name}}
  :host "{{name}}"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :mouse-moved mouse-moved
  :key-pressed key-pressed
  :middleware [m/fun-mode])
