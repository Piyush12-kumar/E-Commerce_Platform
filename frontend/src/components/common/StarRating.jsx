import { HiStar } from 'react-icons/hi';

export default function StarRating({ rating, size = 16 }) {
  return (
    <div className="flex gap-0.5">
      {[1, 2, 3, 4, 5].map((star) => (
        <HiStar key={star} size={size} className={star <= Math.round(rating) ? 'text-yellow-400' : 'text-gray-300'} />
      ))}
    </div>
  );
}

