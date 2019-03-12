import sys
import os

jarpath = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'javalib/PlotJy-0.1.0-SNAPSHOT.jar')
if not jarpath in sys.path:
    sys.path.append(jarpath)

import jyplot
from .jyplot import *

__all__ = []
__all__ += jyplot.__all__