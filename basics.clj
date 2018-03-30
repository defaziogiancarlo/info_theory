; used for update-values
; https://stackoverflow.com/questions/1676891/mapping-a-function-on-the-values-of-a-map-in-clojure

;
; basic helper functions and values
;

; map function f over the keys of map m
(defn update-values [f m]
  (reduce-kv (fn [m k v]
     (assoc m k (f v))) {} m))

; constant value natural logarithm of 2
(def ln2
  (Math/log 2))

; log base 2
(defn lg [x]
  (/ (Math/log x) ln2))

; nlogn for log base 2
(defn nlgn [n]
  (* n (lg n)))


;
; information entropy functions
;


; takes a sequence of probabilities
; assumes independent events
; does not verify probabilities add to one
(defn entropyFromProbabilities [ps]
  (- (reduce + (map nlgn ps))))


; probabilities of occuring for all present symbols
; symbols not seen are assumed not to be in alphabet
(defn probabilities [message]
   (let [s (count message)
         m (frequencies message)]
         (update-values #(/ % s) m)))

; given a collection of probabilities calculates the
; average self-information, or entropy generated by the source
(def entropy
  (comp entropyFromProbabilities vals probabilities))

;;
;; uniquely decodable determination
;;


; if p is a prefix of s, remove p from the beginning of s, otherwise nil
(defn get-suffix [p s]
  (if (clojure.string/starts-with? s p)
      (subs s (count p))
       nil))

; swaps order of starts-with?
(defn is-start-of? [p s]
  (clojure.string/starts-with? s p))

; get all non-zero length suffixes in in codewords for prefix c
(defn dangling-suffixes [codewords c]
  (filter seq (map (partial get-suffix c) codewords)))

; given some codewords, find all new dangling suffixes
; checks all codewords against all other codewords
; this in O(n^2)
(defn all-new-suffixes [codewords]
  (apply hash-set (reduce clojure.set/union
    (map (partial dangling-suffixes codewords) codewords))))


; true if uniquely decodable, otherwise false
(defn uniquely-decodable? [codewords]
  ; keep finding new dangling prefixes until:
  ; no new ones can be found -> true
  ; some dangling prefix is a codeword -> false
  (defn uniquely-decodable-rec? [suffixes]
    (let [new-suffixes (all-new-suffixes (clojure.set/union codewords suffixes))]
      (cond
        (= new-suffixes suffixes) true
        (seq (clojure.set/intersection new-suffixes codewords)) false
        :else (uniquely-decodable-rec? new-suffixes))))
  (uniquely-decodable-rec? #{}))