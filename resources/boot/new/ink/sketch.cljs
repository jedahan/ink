(ns {{name}}.sketch
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

; the setup function run once, and returns the initial state
(defn setup []
  (q/frame-rate 30) ; target 30 frames per second
  {:radius 10
   :green 0
   :min-radius 25
   :center [(/ (q/width) 2) (/ (q/height) 2)]
   :debug false
  })

; mouse-moved runs every time the mouse is moved
; and gets passed an object with the current mouse x and y, and previous mouse x and y
(defn mouse-moved [state mouse]
  (let [radius (max (:min-radius state) (/ (:x mouse) 2))
        green (q/map-range (:y mouse) 0 (q/width) 0 255)]
    (assoc state :radius radius :green green)
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
  (q/background 220 220 120)
  (q/fill 255 (:green state) 255)
  (q/with-translation (:center state)
    (q/ellipse (q/sin (:time state)) (q/cos (:time state)) (:radius state) (:radius state))
  )
  (if (:debug state)
    (let [x 0 y (- (q/height) 16)]
      (q/fill 255 255 255)
      (q/text-size 20)
      (q/text "time " x y)
      (q/text-num (:time state) (+ 40 x) y))))

(q/defsketch {{name}}
  :host "{{name}}"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :mouse-moved mouse-moved
  :key-pressed key-pressed
  :middleware [m/fun-mode])
