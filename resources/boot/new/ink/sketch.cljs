(ns {{name}}
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

; the setup function run once, and returns the initial state
(defn setup []
  (q/frame-rate 30) ; target 30 frames per second
  {
   :green 0
   :min-radius 25
   :center [(/ (q/width) 2) (/ (q/height) 2)]
  })

; mouse-moved runs every time the mouse is moved
; and gets passed an object with the current mouse x and y, and previous mouse x and y
(defn mouse-moved [state mouse]
  (let [radius (max (:min-radius state) (/ (:x mouse) 2))
        green (q/map (:y mouse) 0 (q/width) 0 255)]
    (assoc state :radius radius :green green)
  ))

; update-state runs before every frame
(defn update-state [state] state)

(defn draw-state [state]
  (q/background 220 220 120)
  (q/fill 255 (:green state) 255)
  (q/with-translation (:center state)
    (q/circle (:radius state))
  ))

(q/defsketch {{name}}
  :host "{{name}}"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :mouse-moved mouse-moved
  :middleware [m/fun-mode])
