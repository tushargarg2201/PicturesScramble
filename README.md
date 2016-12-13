# PicturesScramble: PicturesScramble is the game where users need to tell the correct postion of previous original image. 
1. Used Flick API to get the data.
2. Sent the network request through Volley for communicating with Flickr API.
3. Made LR1 cache for storing the bitmap. LR2 cache is not recommended by Volley. 
4. After 15 seconds all the original images will be flipped with dummy image.
5. Users need to tell the correct position of an image which is shown on top.
6. After completion of all the flipped images, it will throw and dialog with the actual clicks (attempts). Worst time attempt score is 36.
